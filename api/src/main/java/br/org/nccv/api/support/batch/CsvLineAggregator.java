package br.org.nccv.api.support.batch;

import org.springframework.batch.item.file.transform.DelimitedLineAggregator;

import java.util.Objects;

/**
 * Responsible for process csv aggregation.
 */
public class CsvLineAggregator<T> extends DelimitedLineAggregator<T> {

  /**
   * Aggregate in the same row the fields.
   *
   * @param fields Fields to be aggregated
   * @return Line with fields
   */
  @Override
  public String doAggregate(Object[] fields) {
    Objects.requireNonNull(fields, "Fields must not be null");
    StringBuilder builder = new StringBuilder();

    for (int index = 0; index < fields.length; ++index) {
      if (index > 0) {
        builder.append(",");
      }

      builder.append("\"");
      builder.append(fields[index]);
      builder.append("\"");
    }

    return builder.toString();
  }

}
