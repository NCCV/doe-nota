package br.org.nccv.api.invoice;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Decorates a invoice transforming values.
 */
public class InvoiceCsv {

  private Invoice invoice;

  /**
   * Create a new invoice csv decorator.
   *
   * @param invoice Invoice
   */
  public InvoiceCsv(Invoice invoice) {
    this.invoice = invoice;
  }

  public String getCnpj() {
    return invoice.getCnpj();
  }

  public String getKey() {
    return invoice.getKey();
  }

  public String getCoo() {
    return invoice.getCoo();
  }

  public String getDate() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return dateTimeFormatter.format(invoice.getDate());
  }

  public String getValue() {
    Locale locale = new Locale("pt", "BR");
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    numberFormatter.setMinimumFractionDigits(2);
    numberFormatter.setMaximumFractionDigits(2);

    return numberFormatter.format(invoice.getValue());
  }

  public String getType() {
    if (StringUtils.isNotBlank(invoice.getCoo())) {
      return "CF";
    }

    return "CFeSAT";
  }
}
