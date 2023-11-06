package com.postech.fase3parquimetro.parking.service;

import com.postech.fase3parquimetro.parking.model.ParkingCreateOrUpdateRecord;
import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import com.postech.fase3parquimetro.parking.model.StatusEnum;
import com.postech.fase3parquimetro.parking.repository.ParkingRepository;
import com.postech.fase3parquimetro.receipt.model.ReceiptEntity;
import com.postech.fase3parquimetro.receipt.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class ParkingService {

    private final int FIVE_MINUTES = 5 * 60 * 1000;

    private final ParkingRepository parkingRepository;
    private final ReceiptService receiptService;

    @Cacheable(value = "parkings", key = "#parkingRequest")
    public ParkingEntity save(ParkingCreateOrUpdateRecord parkingRequest) {
        final var parkingEntity = ParkingEntity.from(parkingRequest);

        return parkingRepository.save(parkingEntity);
    }

    public void update(ParkingEntity parking) {
        parkingRepository.save(parking);
    }

    @Scheduled(fixedRate = FIVE_MINUTES)
    public void checkExpiration() {
        log.info("Checking expiration of parkings");
        final var parkingList =
                parkingRepository.findActiveOrNearExpirationStatus()
                        .orElse(null);

        if (Objects.isNull(parkingList) || parkingList.isEmpty()) {
            return;
        }

        parkingList.forEach(this::controlParkingTime);
    }

    private void controlParkingTime(ParkingEntity parking) {
        final var isAutomaticExtension = parking.isExtendActive();

        if (parking.getStatus().equals(StatusEnum.NEAR_EXPIRATION) && isAutomaticExtension) {
            log.info("{} has been extended for {}", parking.getId(), parking.getDurationInMinutes());
            parking.extendPeriod();
            parking.setStatus(StatusEnum.ACTIVE);
            return;
        }

        if (isNearExpiration(parking)) {
            log.info("{} will expire in 5 minutes", parking.getId());
            parking.setStatus(StatusEnum.NEAR_EXPIRATION);
        } else if (parkingHasExpired(parking)) {
            parking.setStatus(StatusEnum.EXPIRED);

            final var receiptCreation = ReceiptEntity.builder()
                    .createdAt(LocalDateTime.now())
                    .parking(parking).build();

            final var receipt = receiptService.save(receiptCreation);
            log.info("Receipt created: {}", receipt.getId());
        }
    }

    private boolean isNearExpiration(ParkingEntity parking) {
        final var duration = parking.getDurationInMinutes();
        final var durationEndingTimeInMilis = parking.getCreatedAt().plusMinutes(duration).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        final var expiresInFiveMinutesInMilis = LocalDateTime.now().minusMinutes(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        final var currentDurationInMilis = parking.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return durationEndingTimeInMilis - currentDurationInMilis <= expiresInFiveMinutesInMilis;
    }

    @CacheEvict(value = "parkings", allEntries = false, key = "#parking.id")
    private boolean parkingHasExpired(ParkingEntity parking) {
        final var duration = parking.getDurationInMinutes();
        final var durationEndingTime = parking.getCreatedAt().plusMinutes(duration);

        return durationEndingTime.isAfter(LocalDateTime.now()) || durationEndingTime.isEqual(LocalDateTime.now());
    }

    public List<ParkingEntity> getAllParkings() {
        return parkingRepository.findAll();
    }

}
