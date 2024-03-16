package com.protvino.splitwise.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.Value;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Value
public class SqlFilters {

    /**
     * Пороговое число значений в конструкции IN. Если число значений в коллекции больше, то они
     * будут разбиты в запросе на пачки размером в 900 значений (или меньше в случае последней пачки).
     * Это сделано из-за особенностей реализации MariaDB - если число значений в IN будет больше 1000, то
     * СУБД создаст временную таблицу, что сильно ухудшит производительность.
     */
    static final int IN_STATEMENT_THRESHOLD = 900;

    SqlParameterSource params;
    String predicate;

    private SqlFilters(SqlParameterSource params, String predicate) {
        this.params = params;
        this.predicate = predicate;
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(predicate);
    }

    public String makeWhereClause() {
        return StringUtils.isNotEmpty(predicate) ? "\nWHERE " + predicate : "";
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Простой сборщик предиката для фильтрации строк в таблице.
     * Между выражениями возможен только оператор AND.
     */
    public static class Builder {

        private final MapSqlParameterSource params = new MapSqlParameterSource();
        private final ImmutableList.Builder<String> predicateBuilder = ImmutableList.builder();

        private Builder() {
        }

        public Builder eq(String columnName, Number value) {
            if (null != value) {
                addPredicateAndParameterForEq(columnName, value);
            }
            return this;
        }

        private void addPredicateAndParameterForEq(String columnName, Object value) {
            var paramName = columnName + "Eq";
            predicateBuilder.add(columnName + " = " + param(paramName));
            params.addValue(paramName, value);
        }

        private String param(String columnName) {
            return ":" + columnName;
        }

        public Builder eq(String columnName, Boolean value) {
            if (null != value) {
                addPredicateAndParameterForEq(columnName, value);
            }
            return this;
        }

        public Builder eq(String columnName, String value) {
            if (isNotBlank(value)) {
                addPredicateAndParameterForEq(columnName, value);
            }
            return this;
        }

        public Builder like(String columnName, String value) {
            if (isNotBlank(value)) {
                var paramName = columnName + "Like";
                predicateBuilder.add(columnName + " LIKE " + param(paramName));
                params.addValue(paramName, "%" + value + "%");
            }
            return this;
        }

        public <T> Builder in(String columnName, Collection<T> values) {
            if (isNotEmpty(values)) {
                if (values.size() > IN_STATEMENT_THRESHOLD) {
                    Iterable<List<T>> partitions = Iterables.paddedPartition(values, IN_STATEMENT_THRESHOLD);
                    List<String> partitionStringList = new ArrayList<>();

                    partitions.forEach(partition -> {
                        var paramName = String.format("%sIn%d", columnName, (partitionStringList.size() + 1));
                        partitionStringList.add(columnName + " IN(" + param(paramName) + ")");
                        addParameterValuesForInOperator(paramName, partition);
                    });

                    String statement = String.join(" OR ", partitionStringList);
                    predicateBuilder.add(String.format("(%s)", statement));
                } else {
                    var paramName = columnName + "In";
                    predicateBuilder.add(columnName + " IN(" + param(paramName) + ")");
                    addParameterValuesForInOperator(paramName, values);
                }
            }
            return this;
        }

        private <T> void addParameterValuesForInOperator(String paramName, Collection<T> values) {
            if (values.toArray()[0] instanceof Number) {
                params.addValue(paramName, values);
            } else {
                var valuesString = values.stream()
                    .map(String::valueOf)
                    .collect(toImmutableList());
                params.addValue(paramName, valuesString);
            }
        }

        @lombok.Builder
        public record ValuesForInFirstOrSecondOperator<T1, T2>(String firstColumnName, String secondColumnName,
                                                               Collection<T1> firstValues, Collection<T2> secondValues) {}

        public <T1, T2> Builder inFirstOrSecond(ValuesForInFirstOrSecondOperator<T1, T2> values) {
            if (isNotEmpty(values.firstValues) && isNotEmpty(values.secondValues)) {
                var firstParamName = values.firstColumnName + "In";
                var secondParamName = values.secondColumnName + "In";
                var condition = "(%s IN(%s) OR %s IN(%s))".formatted(values.firstColumnName, param(firstParamName),
                    values.secondColumnName, param(secondParamName));
                predicateBuilder.add(condition);
                addParameterValuesForInOperator(firstParamName, values.firstValues);
                addParameterValuesForInOperator(secondParamName, values.secondValues);
            }
            return this;
        }

        public Builder gte(String columnName, Object value) {
            if (null != value) {
                var paramName = columnName + "Gte";
                predicateBuilder.add(columnName + " >= " + param(paramName));
                params.addValue(paramName, value);
            }
            return this;
        }

        public Builder gt(String columnName, Object value) {
            if (null != value) {
                var paramName = columnName + "Gt";
                predicateBuilder.add(columnName + " > " + param(paramName));
                params.addValue(paramName, value);
            }
            return this;
        }

        public Builder lte(String columnName, Object value) {
            if (null != value) {
                var paramName = columnName + "Lte";
                predicateBuilder.add(columnName + " <= " + param(paramName));
                params.addValue(paramName, value);
            }
            return this;
        }

        public Builder lt(String columnName, Object value) {
            if (null != value) {
                var paramName = columnName + "Lt";
                predicateBuilder.add(columnName + " < " + param(paramName));
                params.addValue(paramName, value);
            }
            return this;
        }

        public Builder isNull(String columnName) {
            predicateBuilder.add(columnName + " IS NULL");
            return this;
        }

        public Builder isNotNull(String columnName) {
            predicateBuilder.add(columnName + " IS NOT NULL");
            return this;
        }

        public Builder search(String value, String... columnNames) {
            return search(value, columnNames, null);
        }

        /**
         * Поиск записи по полю поиска.
         * Можно искать по полному и частичному совпадению с переданными колонками.
         * По возможности стоит искать через equals, т.к. это быстрее при наличии b-tree индекса на колонках.
         * // TODO поиск быстрее выполняется по префиксному лайку: LIKE 'prefix%'
         *
         * @param value                искомое значение
         * @param columnNamesForLikes  колонки для поиска по LIKE '%string%'
         * @param columnNamesForEquals колонки для поиска по полному совпадению
         * @return SqlFilters.Builder
         */
        public Builder search(String value, String[] columnNamesForLikes, String[] columnNamesForEquals) {
            if (StringUtils.isNotBlank(value)) {

                boolean hasColumnsForLikes = ArrayUtils.isNotEmpty(columnNamesForLikes);
                boolean hasColumnsForEquals = ArrayUtils.isNotEmpty(columnNamesForEquals);

                String[] searchPredicate = new String[5];

                if (hasColumnsForLikes) {
                    searchPredicate[1] = searchByLikes(value, columnNamesForLikes);
                }
                if (hasColumnsForLikes && hasColumnsForEquals) {
                    searchPredicate[0] = "(";
                    searchPredicate[2] = " OR ";
                    searchPredicate[4] = ")";
                }
                if (hasColumnsForEquals) {
                    searchPredicate[3] = searchByEquals(value, columnNamesForEquals);
                }
                predicateBuilder.add(stream(searchPredicate).filter(Objects::nonNull).collect(joining()));
            }
            return this;
        }

        /**
         * Условное выражение if.
         *
         * @param condition Условие
         * @param isTrue    Действие применяемое, когда условие == true
         * @param isFalse   Действие применяемое, когда условие == false
         * @return Результат выражение isTrue, либо isFalse
         */
        public Builder ifStatement(boolean condition, UnaryOperator<Builder> isTrue, UnaryOperator<Builder> isFalse) {
            return condition ? isTrue.apply(this) : isFalse.apply(this);
        }

        /**
         * Выражение, которое будет применено, только если условие верно.
         *
         * @param condition Условие
         * @param isTrue    Действие применяемое, когда условие == true
         * @return Результат выражение isTrue, либо this
         */
        public Builder ifTrue(boolean condition, UnaryOperator<Builder> isTrue) {
            return condition ? isTrue.apply(this) : this;
        }

        /**
         * Пользовательское sql выражение.
         *
         * @param predicate sql выражение
         * @param values    параметры и значения
         */
        public Builder custom(String predicate, Map<String, ?> values) {
            predicateBuilder.add(predicate);
            params.addValues(values);
            return this;
        }

        private String searchByEquals(String value, String[] columnNames) {
            var paramName = columnNames[0] + "ByEquals";
            var predicate = "" +
                "(" +
                stream(columnNames)
                    .map(columnName -> columnName + " = " + param(paramName))
                    .collect(joining(" OR ")) +
                ")";
            params.addValue(paramName, value);
            return predicate;
        }

        private String searchByLikes(String value, String[] columnNames) {
            var paramName = columnNames[0] + "ByLikes";
            var predicate = "" +
                "(" +
                stream(columnNames)
                    .map(columnName -> columnName + " LIKE " + param(paramName))
                    .collect(joining(" OR ")) +
                ")";
            params.addValue(paramName, "%" + value + "%");
            return predicate;
        }

        public SqlFilters build() {
            var predicate = String.join(" AND ", predicateBuilder.build());
            return new SqlFilters(params, predicate);
        }
    }
}
