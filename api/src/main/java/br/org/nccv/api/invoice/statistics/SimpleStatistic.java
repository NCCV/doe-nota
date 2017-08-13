package br.org.nccv.api.invoice.statistics;

import java.math.BigDecimal;

/**
 * Represent a date and total value.
 */
public interface SimpleStatistic {

  String getDate();

  BigDecimal getValue();
}
