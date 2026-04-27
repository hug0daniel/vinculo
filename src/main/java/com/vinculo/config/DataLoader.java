package com.vinculo.config;

import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.model.PartnerType;
import com.vinculo.domain.user.repository.PartnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final PartnerRepository partnerRepository;

    public DataLoader(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public void run(String... args) {
        if (partnerRepository.count() > 0) {
            log.info("Partners already loaded, skipping");
            return;
        }

        log.info("Loading initial partners...");

        final Partner p1 = new Partner("Cruz Vermelha", PartnerType.NGO, "contact@cruzvermelha.pt", "Lisbon");
        final Partner p2 = new Partner("Proteção Civil", PartnerType.CIVIL_PROTECTION, "protecao@civil.pt", "Porto");
        final Partner p3 = new Partner("Junta de Freguesia", PartnerType.PARISH_COUNCIL, "junta@freguesia.pt", "Coimbra");

        partnerRepository.save(p1);
        partnerRepository.save(p2);
        partnerRepository.save(p3);

        log.info("Loaded 3 partners");
    }
}