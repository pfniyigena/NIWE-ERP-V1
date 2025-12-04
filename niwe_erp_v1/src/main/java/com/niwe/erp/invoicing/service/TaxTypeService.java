package com.niwe.erp.invoicing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.core.domain.CoreCountry;
import com.niwe.erp.core.domain.EFiscalYear;
import com.niwe.erp.core.domain.EState;
import com.niwe.erp.core.repository.CoreCountryRepository;
import com.niwe.erp.invoicing.domain.TaxType;
import com.niwe.erp.invoicing.repository.TaxTypeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TaxTypeService {

	private TaxTypeRepository taxTypeRepository;
	private CoreCountryRepository coreCountryRepository;

	public List<TaxType> findAllActive() {
		return taxTypeRepository.findByState(EState.ACTIVE);
	}

	List<TaxType> findByIsDefault(Boolean isDefault) {
		return taxTypeRepository.findByIsDefault(isDefault);
	}

	public TaxType save(TaxType taxType) {
		TaxType exist = null;
		if (taxType.getId() != null) {
			exist = taxTypeRepository.getReferenceById(taxType.getId());
			exist.setTaxName(taxType.getTaxName());
			exist.setTaxCode(taxType.getTaxCode());
			exist.setTaxValue(taxType.getTaxValue());
			exist.setDescription(taxType.getDescription());
			exist.setDisplayName(taxType.getDisplayName());
		} else {
			exist = taxType;
			exist.setDisplayLevel(findAll().size());
		}

		return taxTypeRepository.save(exist);

	}

	public TaxType findById(String id) {

		return taxTypeRepository.getReferenceById(UUID.fromString(id));
	}

	public TaxType findByCode(String taxCode) {

		return taxTypeRepository.findByTaxCode(taxCode);
	}

	public void initializeTaxes(EFiscalYear fiscalYear) {

		if (fiscalYear != null && findAll().isEmpty()) {
			switch (fiscalYear) {
			case RWANDA: {

				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("A").displayName("A-EX").taxCode("A").taxValue(new BigDecimal("0.00"))
								.isDefault(Boolean.FALSE).displayLevel(0).build(),
						TaxType.builder().taxName("B").displayName("B").taxCode("B").taxValue(new BigDecimal("18.00"))
								.isDefault(Boolean.TRUE).displayLevel(1).build(),
						TaxType.builder().state(EState.INACTIVE).taxName("C").displayName("C").taxCode("C")
								.taxValue(new BigDecimal("0.00")).isDefault(Boolean.FALSE).displayLevel(2).build(),
						TaxType.builder().state(EState.INACTIVE).taxName("D").displayName("D").taxCode("D")
								.taxValue(new BigDecimal("0.00")).isDefault(Boolean.FALSE).displayLevel(3).build()));
				coreCountryRepository.save(CoreCountry.builder().code("RW").displayName(fiscalYear.name())
						.englishName(fiscalYear.name()).frenchName(fiscalYear.name()).isDefault(Boolean.TRUE).build());

				break;
			}
			case CHAD: {
				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("EX").displayName("Exonere").taxCode("EX").isDefault(Boolean.FALSE)
								.taxValue(new BigDecimal("0.00")).displayLevel(0).build(),
						TaxType.builder().taxName("TN").displayName("Taux Normal").taxCode("TN")
								.isDefault(Boolean.FALSE).taxValue(new BigDecimal("19.25")).displayLevel(1).build(),
						TaxType.builder().taxName("TR").displayName("Taux Reduit").taxCode("TR").isDefault(Boolean.TRUE)
								.taxValue(new BigDecimal("9.9")).displayLevel(2).build(),
						TaxType.builder().taxName("XP").displayName("Exportation").taxCode("XP")
								.isDefault(Boolean.FALSE).taxValue(new BigDecimal("0.00")).displayLevel(3).build(),
						TaxType.builder().taxName("NA").displayName("Non assugetti").taxCode("NA")
								.isDefault(Boolean.FALSE).taxValue(new BigDecimal("0.00")).displayLevel(4).build()));

				coreCountryRepository.save(CoreCountry.builder().code("TD").displayName(fiscalYear.name())
						.englishName(fiscalYear.name()).frenchName(fiscalYear.name()).isDefault(Boolean.TRUE).build());
				break;
			}
			case GENERAL: {
				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("A").displayName("A-EX").taxCode("A").taxValue(new BigDecimal("0.00"))
								.displayLevel(0).isDefault(Boolean.FALSE).build(),
						TaxType.builder().taxName("VAT").displayName("VAT").taxCode("VAT").isDefault(Boolean.TRUE)
								.taxValue(new BigDecimal("18.00")).displayLevel(1).build()));
				coreCountryRepository.save(CoreCountry.builder().code("RW").displayName(fiscalYear.name())
						.englishName(fiscalYear.name()).frenchName(fiscalYear.name()).isDefault(Boolean.TRUE).build());
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fiscalYear);
			}

		}

	}

	public List<TaxType> findAll() {
		return taxTypeRepository.findAll();
	}

}
