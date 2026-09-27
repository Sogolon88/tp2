package tp2.ibm.dto;

import java.util.Map;

public record BMIStatistics(long totalEnregistrements, Double bmiMoyen, Map<String, Long> repartition) {}