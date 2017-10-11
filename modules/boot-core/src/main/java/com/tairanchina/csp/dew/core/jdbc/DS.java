package com.tairanchina.csp.dew.core.jdbc;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.StandardCode;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.entity.EntityContainer;
import com.tairanchina.csp.dew.core.jdbc.dialect.Dialect;
import com.tairanchina.csp.dew.core.jdbc.dialect.DialectFactory;
import com.tairanchina.csp.dew.core.jdbc.dialect.DialectType;
import com.tairanchina.csp.dew.core.jdbc.proxy.MethodConstruction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DS {

    private static final Logger logger = LoggerFactory.getLogger(DS.class);

    private static final String FIELD_PLACE_HOLDER_REGEX = "\\#\\{\\s*\\w+\\s*\\}"; // 正则匹配 #{key}
    private static final Pattern FIELD_PLACE_HOLDER_PATTERN = Pattern.compile(FIELD_PLACE_HOLDER_REGEX);

    private static final char UNDERLINE = '_';
    private static final String STAR = "*";
    private static final String POINT = ".";
    private static final String EMPTY = "";
    private String leftDecorated;
    private String rightDecorated;
    private JdbcTemplate jdbcTemplate;
    private String jdbcUrl;

    private Dialect dialect;

    private void init() {
        if (StringUtils.isEmpty(jdbcUrl)){
            leftDecorated = "`";
            rightDecorated = "`";
            return;
        }
        dialect = DialectFactory.parseDialect(jdbcUrl);
        switch (dialect.getDialectType()) {
            case H2:
                leftDecorated = "`";
                rightDecorated = "`";
                break;
            case MYSQL:
                leftDecorated = "`";
                rightDecorated = "`";
                break;
            case ORACLE:
                leftDecorated = "\"";
                rightDecorated = "\"";
                break;
            case POSTGRE:
                leftDecorated = "\"";
                rightDecorated = "\"";
                break;
            case SQLSERVER:
                leftDecorated = "[";
                rightDecorated = "]";
                break;
            case DB2:
                leftDecorated = "[";
                rightDecorated = "]";
                break;
            case PHOENIX: // TODO
                leftDecorated = "[";
                rightDecorated = "]";
                break;
            default:
        }
    }

    public JdbcTemplate jdbc() {
        return jdbcTemplate;
    }

    public Object insert(Object entity) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entity.getClass());
        Object[] packageInsert = packageInsert(new ArrayList<Object>() {{
            add(entity);
        }}, true);
        String sql = (String) packageInsert[0];
        Object[] args = ((List<Object[]>) packageInsert[1]).get(0);
        if (entityClassInfo.pkFieldNameOpt.isPresent() &&
                entityClassInfo.pkUUIDOpt.isPresent() && entityClassInfo.pkUUIDOpt.get()) {
            jdbcTemplate.update(sql, args);
            return ((Optional<String>) packageInsert[2]).get();
        } else if (entityClassInfo.pkFieldNameOpt.isPresent()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{entityClassInfo.pkFieldNameOpt.get()});
                PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args);
                pss.setValues(ps);
                return ps;
            }, keyHolder);
            return keyHolder.getKey();
        } else {
            jdbcTemplate.update(sql, args);
            return 0;
        }
    }

    public void insert(Iterable<?> entities) {
        Object[] packageInsert = packageInsert(entities, false);
        jdbcTemplate.batchUpdate((String) packageInsert[0], (List<Object[]>) packageInsert[1]);
    }

    public int updateById(Object id, Object entity) {
        try {
            $.bean.setValue(entity, EntityContainer.getEntityClassByClazz(entity.getClass()).pkFieldNameOpt.get(), id);
            Object[] packageUpdate = packageUpdate(entity, true);
            return jdbcTemplate.update((String) packageUpdate[0], (Object[]) packageUpdate[1]);
        } catch (NoSuchFieldException e) {
            logger.error("UpdateById error", e);
            return 0;
        }
    }

    public int updateByCode(String code, Object entity) {
        try {
            $.bean.setValue(entity, EntityContainer.getEntityClassByClazz(entity.getClass()).codeFieldNameOpt.get(), code);
            Object[] packageUpdate = packageUpdate(entity, true);
            return jdbcTemplate.update((String) packageUpdate[0], (Object[]) packageUpdate[1]);
        } catch (NoSuchFieldException e) {
            logger.error("updateByCode error", e);
            return 0;
        }
    }

    public <E> E getById(Object id, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return get(SB.inst()
                .eqVL(entityClassInfo.pkFieldNameOpt.get(), id), entityClazz);
    }

    public <E> E getByCode(String code, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return get(SB.inst()
                .eqVL(entityClassInfo.codeFieldNameOpt.get(), code), entityClazz);
    }

    public <E> E get(SB sqlBuilder, Class<E> entityClazz) {
        Object[] packageSelect = packageSelect(entityClazz, sqlBuilder);
        return convertRsToObj(jdbcTemplate.queryForMap((String) packageSelect[0], (Object[]) packageSelect[1]), entityClazz);
    }

    public int deleteById(Object id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return delete(SB.inst()
                .eqVL(entityClassInfo.pkFieldNameOpt.get(), id), entityClazz);
    }

    public int deleteByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return delete(SB.inst()
                .eqVL(entityClassInfo.codeFieldNameOpt.get(), code), entityClazz);
    }

    public int deleteAll(Class<?> entityClazz) {
        return delete(SB.inst(), entityClazz);
    }

    public int delete(SB sqlBuilder, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
        return jdbcTemplate.update(String.format("DELETE FROM " + leftDecorated + "%s" + rightDecorated + " %s",
                entityClassInfo.tableName, sb[0]),
                ((List) sb[1]).toArray());
    }

    public int enableById(Object id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return enable(SB.inst()
                .eqVL(entityClassInfo.pkFieldNameOpt.get(), id), entityClazz);
    }

    public int enableByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return enable(SB.inst().eqVL(entityClassInfo.codeFieldNameOpt.get(), code), entityClazz);
    }

    public int enable(SB sqlBuilder, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
        EntityContainer.EntityClassInfo.Column column = entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get());
        ((List) sb[1]).add(0, !column.reverse);
        return jdbcTemplate.update(String.format("UPDATE %s SET " + leftDecorated + "%s" + rightDecorated + " = ? %s",
                entityClassInfo.tableName,
                column.columnName,
                sb[0]),
                ((List) sb[1]).toArray());
    }

    public int disableById(Object id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return disable(SB.inst()
                .eqVL(entityClassInfo.pkFieldNameOpt.get(), id), entityClazz);
    }

    public int disableByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return disable(SB.inst()
                .eqVL(entityClassInfo.codeFieldNameOpt.get(), code), entityClazz);
    }

    public int disable(SB sqlBuilder, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
        EntityContainer.EntityClassInfo.Column column = entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get());
        ((List) sb[1]).add(0, column.reverse);
        return jdbcTemplate.update(String.format("UPDATE %s SET " + leftDecorated + "%s" + rightDecorated + " = ? %s",
                entityClassInfo.tableName,
                column.columnName,
                sb[0]),
                ((List) sb[1]).toArray());
    }

    public boolean existById(Object id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return exist(SB.inst()
                .eqVL(entityClassInfo.pkFieldNameOpt.get(), id), entityClazz);
    }

    public boolean existByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return exist(SB.inst()
                .eqVL(entityClassInfo.codeFieldNameOpt.get(), code), entityClazz);
    }

    public boolean exist(SB sqlBuilder, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM " + leftDecorated + "%s" + rightDecorated + " %s",
                entityClassInfo.tableName,
                sb[0]),
                ((List) sb[1]).toArray(), Long.class) != 0;
    }

    public <E> List<E> findAll(Class<E> entityClazz) {
        return find(SB.inst(), entityClazz);
    }

    public <E> List<E> findEnabled(Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return find(SB.inst().eqVL(entityClassInfo.enabledFieldNameOpt.get(), true), entityClazz);
    }

    public <E> List<E> findDisabled(Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return find(SB.inst().eqVL(entityClassInfo.enabledFieldNameOpt.get(), false), entityClazz);
    }

    public <E> List<E> find(SB sqlBuilder, Class<E> entityClazz) {
        Object[] packageSelect = packageSelect(entityClazz, sqlBuilder);
        return jdbcTemplate.queryForList((String) packageSelect[0], (Object[]) packageSelect[1]).stream()
                .map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
    }

    public <E> List<E> find(String sql, Object[] params, Class<E> entityClazz) {
        return jdbcTemplate.queryForList(sql, params).stream()
                .map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
    }

    public long countAll(Class<?> entityClazz) {
        return count(SB.inst(), entityClazz);
    }

    public long countEnabled(Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        EntityContainer.EntityClassInfo.Column column = entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get());
        return count(SB.inst()
                .eqVL(entityClassInfo.enabledFieldNameOpt.get(), !column.reverse), entityClazz);
    }

    public long countDisabled(Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        EntityContainer.EntityClassInfo.Column column = entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get());
        return count(SB.inst()
                .eqVL(entityClassInfo.enabledFieldNameOpt.get(), column.reverse), entityClazz);
    }

    public long count(SB sqlBuilder, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM " + leftDecorated + "%s" + rightDecorated + " %s",
                entityClassInfo.tableName,
                sb[0]),
                ((List) sb[1]).toArray(), Long.class);
    }

    public <E> Page<E> paging(long pageNumber, int pageSize, Class<E> entityClazz) {
        return paging(SB.inst(), pageNumber, pageSize, entityClazz);
    }

    public <E> Page<E> pagingEnabled(long pageNumber, int pageSize, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return paging(SB.inst()
                .eqVL(entityClassInfo.enabledFieldNameOpt.get(), true), pageNumber, pageSize, entityClazz);
    }

    public <E> Page<E> pagingDisabled(long pageNumber, int pageSize, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        return paging(SB.inst()
                .eqVL(entityClassInfo.enabledFieldNameOpt.get(), false), pageNumber, pageSize, entityClazz);
    }

    public <E> Page<E> paging(SB sqlBuilder, long pageNumber, int pageSize, Class<E> entityClazz) {
        Object[] packageSelect = packageSelect(entityClazz, sqlBuilder);
        return paging((String) packageSelect[0], (Object[]) packageSelect[1], pageNumber, pageSize, entityClazz);
    }

    public <E> Page<E> paging(String sql, Object[] params, long pageNumber, int pageSize, Class<E> entityClazz) {
        String countSql = wrapCountSql(sql);
        String pagedSql = wrapPagingSql(sql, pageNumber, pageSize);
        long totalRecords = jdbcTemplate.queryForObject(countSql, params, Long.class);
        List<E> objects = jdbcTemplate.queryForList(pagedSql, params).stream()
                .map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
        return Page.build(pageNumber, pageSize, totalRecords, objects);
    }

    public String wrapPagingSql(String oriSql, long pageNumber, int pageSize) {
        return dialect.paging(oriSql, pageNumber, pageSize);
    }

    public String wrapCountSql(String oriSql) {
        return dialect.count(oriSql);
    }

    /**
     * 组装插入SQL
     *
     * @param entities 实体集合
     * @return 格式 Object[]{Sql:String,params:List<Object[]>,id:UUID}
     */
    private Object[] packageInsert(Iterable<?> entities, boolean ignoreNullValue) {
        if (!entities.iterator().hasNext()) {
            throw Dew.E.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Entity List is empty."));
        }
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entities.iterator().next().getClass());
        String sql = null;
        Optional<String> lastIdOpt = Optional.empty();
        List<Object[]> params = new ArrayList<>();
        for (Object entity : entities) {

            Map<String, Object> values = $.bean.findValues(entity, null, null, entityClassInfo.columns.keySet(), null);
            if (entityClassInfo.pkFieldNameOpt.isPresent() && entityClassInfo.pkUUIDOpt.get()) {
                if (!values.containsKey(entityClassInfo.pkFieldNameOpt.get()) ||
                        values.get(entityClassInfo.pkFieldNameOpt.get()) == null ||
                        values.get(entityClassInfo.pkFieldNameOpt.get()).toString().isEmpty()) {
                    lastIdOpt = Optional.of($.field.createUUID());
                    values.put(entityClassInfo.pkFieldNameOpt.get(), lastIdOpt.get());
                } else {
                    lastIdOpt = Optional.of(values.get(entityClassInfo.pkFieldNameOpt.get()).toString());
                }
            } else if (entityClassInfo.pkFieldNameOpt.isPresent() &&
                    values.containsKey(entityClassInfo.pkFieldNameOpt.get())) {
                Object id = values.get(entityClassInfo.pkFieldNameOpt.get());
                if (id == null || id instanceof Number && (int) id == 0) {
                    // Remove private key field
                    values.remove(entityClassInfo.pkFieldNameOpt.get());
                }
            }
            // Add ext values
            if (entityClassInfo.codeFieldNameOpt.isPresent() && entityClassInfo.codeUUIDOpt.get() &&
                    (!values.containsKey(entityClassInfo.codeFieldNameOpt.get()) ||
                            values.get(entityClassInfo.codeFieldNameOpt.get()) == null ||
                            values.get(entityClassInfo.codeFieldNameOpt.get()).toString().isEmpty())) {
                values.put(entityClassInfo.codeFieldNameOpt.get(), $.field.createUUID());
            }
            LocalDateTime now = LocalDateTime.now();
            if (entityClassInfo.createUserFieldNameOpt.isPresent()) {
                if (Dew.context().optInfo().isPresent()) {
                    values.put(entityClassInfo.createUserFieldNameOpt.get(), Dew.context().optInfo().get().getAccountCode());
                } else {
                    values.put(entityClassInfo.createUserFieldNameOpt.get(), EMPTY);
                }
            }
            if (entityClassInfo.createTimeFieldNameOpt.isPresent()) {
                values.put(entityClassInfo.createTimeFieldNameOpt.get(), now);
            }
            if (entityClassInfo.updateUserFieldNameOpt.isPresent()) {
                if (Dew.context().optInfo().isPresent()) {
                    values.put(entityClassInfo.updateUserFieldNameOpt.get(), Dew.context().optInfo().get().getAccountCode());
                } else {
                    values.put(entityClassInfo.updateUserFieldNameOpt.get(), EMPTY);
                }
            }
            if (entityClassInfo.updateTimeFieldNameOpt.isPresent()) {
                values.put(entityClassInfo.updateTimeFieldNameOpt.get(), now);
            }
            // Check null
            if (values.entrySet().stream()
                    .anyMatch(entry -> entityClassInfo.columns.get(entry.getKey()).notNull && entry.getValue() == null)) {
                throw Dew.E.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Not Null check fail."));
            }
            // Filter null value if ignoreNullValue=true
            if (ignoreNullValue) {
                values = values.entrySet().stream()
                        .filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            if (sql == null) {
                // Package
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO ").append(leftDecorated + entityClassInfo.tableName + rightDecorated);
                sb.append(values.entrySet().stream()
                        .map(entry -> leftDecorated + entityClassInfo.columns.get(entry.getKey()).columnName + rightDecorated)
                        .collect(Collectors.joining(", ", " (", ") ")));
                sb.append("VALUES");
                sb.append(values.keySet().stream().map(o -> "?").collect(Collectors.joining(", ", " (", ") ")));
                sql = sb.toString();
            }
            params.add(values.values().toArray());
        }
        return new Object[]{sql, params, lastIdOpt};
    }

    /**
     * 根据主键或Code组装更新SQL
     * 存在主键值时使用主键，否则使用Code，都不存在时报错
     *
     * @param entity          实体
     * @param ignoreNullValue 是否忽略null值插入
     * @return 格式 Object[]{Sql:String,params:Object[]}
     */
    private Object[] packageUpdate(Object entity, boolean ignoreNullValue) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entity.getClass());
        Map<String, Object> values = $.bean.findValues(entity, null, null, entityClassInfo.columns.keySet(), null);
        // Check id or code Not empty
        if (!entityClassInfo.pkFieldNameOpt.isPresent() && !entityClassInfo.codeFieldNameOpt.isPresent()) {
            throw Dew.E.e(StandardCode.NOT_FOUND.toString(), new RuntimeException("Need @PkColumn or @CodeColumn field."));
        }
        String whereColumnName = null;
        Object whereValue = null;
        if (entityClassInfo.pkFieldNameOpt.isPresent()
                && values.get(entityClassInfo.pkFieldNameOpt.get()) != null
                && !values.get(entityClassInfo.pkFieldNameOpt.get()).toString().isEmpty()) {
            if (!"0".equals(values.get(entityClassInfo.pkFieldNameOpt.get()).toString())) {
                whereColumnName = entityClassInfo.columns.get(entityClassInfo.pkFieldNameOpt.get()).columnName;
                whereValue = values.get(entityClassInfo.pkFieldNameOpt.get());
            }
            values.remove(entityClassInfo.pkFieldNameOpt.get());
        }
        if (whereColumnName == null) {
            if (entityClassInfo.codeFieldNameOpt.isPresent()
                    && values.get(entityClassInfo.codeFieldNameOpt.get()) != null
                    && !values.get(entityClassInfo.codeFieldNameOpt.get()).toString().isEmpty()) {
                whereColumnName = entityClassInfo.columns.get(entityClassInfo.codeFieldNameOpt.get()).columnName;
                whereValue = values.get(entityClassInfo.codeFieldNameOpt.get());
                values.remove(entityClassInfo.codeFieldNameOpt.get());
            } else {
                throw Dew.E.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Need Private Key or Code value."));
            }
        }
        // Filter null value if ignoreNullValue=true
        if (ignoreNullValue) {
            values = values.entrySet().stream()
                    .filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        // Add ext values
        if (entityClassInfo.updateUserFieldNameOpt.isPresent()) {
            if (Dew.context().optInfo().isPresent()) {
                values.put(entityClassInfo.updateUserFieldNameOpt.get(), Dew.context().optInfo().get().getAccountCode());
            } else {
                values.put(entityClassInfo.updateUserFieldNameOpt.get(), EMPTY);
            }
        }
        if (entityClassInfo.createUserFieldNameOpt.isPresent()) {
            values.remove(entityClassInfo.createUserFieldNameOpt.get());
        }
        if (entityClassInfo.updateTimeFieldNameOpt.isPresent()) {
            values.put(entityClassInfo.updateTimeFieldNameOpt.get(), LocalDateTime.now());
        }
        if (entityClassInfo.createTimeFieldNameOpt.isPresent()) {
            values.remove(entityClassInfo.createTimeFieldNameOpt.get());
        }
        // Check null
        if (values.entrySet().stream()
                .anyMatch(entry -> entityClassInfo.columns.get(entry.getKey()).notNull && entry.getValue() == null)) {
            throw Dew.E.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Not Null check fail."));
        }
        // Package
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sb.append("UPDATE ").append(leftDecorated + entityClassInfo.tableName + rightDecorated).append(" SET ");
        sb.append(values.entrySet().stream()
                .map(entry -> {
                    params.add(entry.getValue());
                    return leftDecorated + entityClassInfo.columns.get(entry.getKey()).columnName + "` = ?";
                })
                .collect(Collectors.joining(", ")));
        sb.append(String.format(" WHERE " + leftDecorated + "%s" + rightDecorated + " = ?", whereColumnName));
        params.add(whereValue);
        return new Object[]{sb.toString(), params.toArray()};
    }

    /**
     * 组装查询SQL
     *
     * @param entityClazz 实体类型
     * @param sqlBuilder  SQL构造器
     * @return 格式 Object[]{Sql:String,params:Object[]}
     */
    private Object[] packageSelect(Class<?> entityClazz, SB sqlBuilder) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        StringBuilder sql = new StringBuilder();
        Object[] params = new Object[]{};
        sql.append("SELECT ");
        sql.append(entityClassInfo.columns.values().stream()
                .map(col -> leftDecorated + col.columnName + rightDecorated).collect(Collectors.joining(", ")));
        sql.append(" FROM ").append(leftDecorated + entityClassInfo.tableName + rightDecorated);
        if (sqlBuilder != null) {
            Object[] sb = sqlBuilder.build(entityClassInfo, leftDecorated, rightDecorated);
            sql.append(sb[0]);
            params = ((List) sb[1]).toArray();
        }
        return new Object[]{sql.toString(), params};
    }

    /**
     * 将ResultSet转成对象
     *
     * @param rs          ResultSet(Map格式)
     * @param entityClazz 对象类型
     * @return 转换后的对象
     */
    public <E> E convertRsToObj(Map<String, Object> rs, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(entityClazz);
        try {
            E entity = entityClazz.newInstance();
            if (entityClassInfo == null) {
                for (Map.Entry<String, Object> entry : rs.entrySet()) {
                    Object r = convertRsToObject(entry);
                    $.bean.setValue(entity, underlineToCamel(entry.getKey().toLowerCase()), r);
                }
            } else {
                for (Map.Entry<String, Object> entry : rs.entrySet()) {
                    if (entityClassInfo.columnRel.containsKey(entry.getKey().toLowerCase())) {
                        Object r = convertRsToObject(entry);
                        $.bean.setValue(entity, entityClassInfo.columnRel.get(entry.getKey().toLowerCase()), r);
                    }
                }
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | IllegalArgumentException e) {
            logger.error("Convert ResultSet to Object error", e);
            return null;
        }
    }

    private Object convertRsToObject(Map.Entry<String, Object> entry) {
        Object r;
        if (entry.getValue() instanceof Timestamp) {
            r = ((Timestamp) entry.getValue()).toLocalDateTime();
        } else if (entry.getValue() instanceof java.sql.Date) {
            r = ((java.sql.Date) entry.getValue()).toLocalDate();
        } else if (entry.getValue() instanceof Time) {
            r = ((Time) entry.getValue()).toLocalTime();
        } else {
            r = entry.getValue();
        }
        return r;
    }

    public <E> List<E> selectForList(Class<E> entityClazz, Map<String, Object> params, String sql) {
        Object[] result = packageSelect(sql, params, dialect.getDialectType());
        List<Map<String, Object>> list = jdbcTemplate.queryForList((String) result[0], (Object[]) result[1]);
        return entityClazz.isAssignableFrom(Map.class) ? (List<E>) list : list.stream().map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
    }

    public <E> Page<E> selectForPaging(Class<E> entityClazz, MethodConstruction method, String sql) {
        Object[] result = packageSelect(sql, method.getParamsMap(), dialect.getDialectType());
        String countSql = wrapCountSql((String) result[0]);
        String pagedSql = wrapPagingSql((String) result[0], method.getPageNumber(), method.getPageSize());
        long totalRecords = jdbcTemplate.queryForObject(countSql, (Object[]) result[1], Long.class);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(pagedSql, (Object[]) result[1]);
        List<E> objects = entityClazz.isAssignableFrom(Map.class) ? (List<E>) list : list.stream().map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
        return Page.build(method.getPageNumber(), method.getPageSize(), totalRecords, objects);
    }

    public static Object[] packageSelect(String sql, Map<String, Object> params, DialectType dialectType) {
        Matcher m = FIELD_PLACE_HOLDER_PATTERN.matcher(sql);
        List<String> matchRegexList = new ArrayList<>();
        //将#{...}抠出来
        while (m.find()) {
            matchRegexList.add(m.group());
        }
        List<Object> list = new ArrayList<>();
        //将值不为空的key用?替换
        for (String key : matchRegexList) {
            // #{key},去掉#{}和空格,获取真实的key
            key = key.substring(2, key.length() - 1).replace(" ", EMPTY);
            Object v = params.get(key);
            if (v != null) {
                sql = sql.replaceFirst("\\#\\{\\s*" + key + "\\s*\\}", "?");
                list.add(v);
            }
        }
        SQLSelectStatement statement;
        switch (dialectType) {
            case H2:
            case MYSQL:
                statement = (SQLSelectStatement) new MySqlStatementParser(sql).parseSelect();
                break;
            case ORACLE:
                statement = (SQLSelectStatement) new OracleStatementParser(sql).parseStatement();
                break;
            case POSTGRE:
                statement = (SQLSelectStatement) new PGSQLStatementParser(sql).parseStatement();
                break;
            case SQLSERVER:
                statement = (SQLSelectStatement) new SQLServerStatementParser(sql).parseStatement();
                break;
            case DB2:
                statement = (SQLSelectStatement) new DB2StatementParser(sql).parseStatement();
                break;
            case PHOENIX:
                statement = (SQLSelectStatement) new PhoenixStatementParser(sql).parseStatement();
                break;
            default:
                statement = (SQLSelectStatement) new SQLStatementParser(sql).parseStatementList().get(0);
        }
        if (sql.contains("#{")) {
            SQLExpr sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
            formatWhere(sqlExpr);
        }
        if (sql.contains(STAR)) {
            SQLTableSource sqlTableSource = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getFrom();
            List<SQLSelectItem> selectList = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getSelectList();
            List<SQLSelectItem> addList = new ArrayList<>();
            formatFrom(sqlTableSource, selectList, addList);
            selectList.addAll(addList);
        }
        sql = statement.toString();
        return new Object[]{sql, list.toArray()};
    }

    /**
     * 格式化select中的 * 为对应table 字段
     */
    private static void formatFrom(SQLTableSource sqlTableSource, List<SQLSelectItem> selectList, List<SQLSelectItem> addList) {
        if (sqlTableSource == null) {
            return;
        }
        if (sqlTableSource instanceof SQLExprTableSource) {
            doFormat((SQLExprTableSource) sqlTableSource, selectList, addList);
        }
        if (sqlTableSource instanceof SQLJoinTableSource) {
            formatFrom(((SQLJoinTableSource) sqlTableSource).getRight(), selectList, addList);
            formatFrom(((SQLJoinTableSource) sqlTableSource).getLeft(), selectList, addList);
        }
    }

    private static void doFormat(SQLExprTableSource sqlTableSource, List<SQLSelectItem> selectList, List<SQLSelectItem> addList) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(((SQLIdentifierExpr) sqlTableSource.getExpr()).getName());
        if (entityClassInfo == null) {
            return;
        }
        Iterator<SQLSelectItem> iterator = selectList.iterator();
        while (iterator.hasNext()) {
            SQLSelectItem sqlSelectItem = iterator.next();
            if (sqlSelectItem.getExpr() instanceof SQLPropertyExpr) {
                SQLPropertyExpr expr = (SQLPropertyExpr) sqlSelectItem.getExpr();
                SQLIdentifierExpr expr_owner = (SQLIdentifierExpr) expr.getOwner();
                if ((expr_owner.getName() + POINT + expr.getName()).equals(sqlTableSource.getAlias() + POINT + STAR)) {
                    iterator.remove();
                    entityClassInfo.columns.forEach((filedName, column) -> addWhenAlias(addList, expr_owner, column));
                }
            } else if (sqlSelectItem.getExpr() instanceof SQLObjectImpl) {
                iterator.remove();
                entityClassInfo.columns.forEach((filedName, column) -> addList.add(new SQLSelectItem(new SQLIdentifierExpr(column.columnName))));
            }
        }
    }

    private static void addWhenAlias(List<SQLSelectItem> addList, SQLIdentifierExpr expr_owner, EntityContainer.EntityClassInfo.Column column) {
        if (column.columnName.equals("id") || column.columnName.equals("created_by") || column.columnName.equals("updated_by") || column.columnName.equals("created_time") ||
                column.columnName.equals("updated_time"))
            return;
        addList.add(new SQLSelectItem(new SQLPropertyExpr(expr_owner.getName(), column.columnName)));
    }


    private static String underlineToCamel(String param) {
        if (param == null || EMPTY.equals(param.trim())) {
            return EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void formatWhere(SQLExpr sqlExpr) {
        if (sqlExpr == null) {
            return;
        }
        if (sqlExpr instanceof SQLBetweenExpr
                || sqlExpr instanceof SQLInListExpr
                || ((SQLBinaryOpExpr) sqlExpr).getLeft() instanceof SQLIdentifierExpr
                || ((SQLBinaryOpExpr) sqlExpr).getLeft() instanceof SQLPropertyExpr) {
            doFormatWhere(sqlExpr);
        } else {
            formatWhere(((SQLBinaryOpExpr) sqlExpr).getRight());
            formatWhere(((SQLBinaryOpExpr) sqlExpr).getLeft());
        }
    }

    private static void doFormatWhere(SQLExpr sqlExpr) {
        String itemStr;
        if (sqlExpr instanceof SQLBetweenExpr) {
            itemStr = ((SQLBetweenExpr) sqlExpr).getBeginExpr().toString() + ((SQLBetweenExpr) sqlExpr).getEndExpr().toString();
        } else if (sqlExpr instanceof SQLInListExpr) {
            itemStr = ((SQLInListExpr) sqlExpr).getTargetList().toString();
        } else {
            itemStr = sqlExpr.toString();
        }
        if (FIELD_PLACE_HOLDER_PATTERN.matcher(itemStr).find()) {
            if (sqlExpr.getParent() instanceof SQLBinaryOpExpr) {
                ((SQLBinaryOpExpr) sqlExpr.getParent()).replace(sqlExpr, null);
            } else if (sqlExpr.getParent() instanceof SQLSelectQueryBlock) {
                ((SQLSelectQueryBlock) sqlExpr.getParent()).replace(sqlExpr, null);
            }
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public static class SB {

        private List<SQLCondition> conditions = new ArrayList<>();
        private Map<String, Boolean> order = new LinkedHashMap<>();
        private List<String> tables = new ArrayList<>();
        private List<SQLJoin> joins = new ArrayList<>();
        private List<String> selectFields = new ArrayList<>();

        private SB() {
        }

        public static SB inst() {
            return new SB();
        }

        public Object[] build(String leftDecorated, String rightDecorated) {
            return build(null, leftDecorated, rightDecorated);
        }

        /**
         * 构建SQL语句 
         * selectFields 和 tables 必须同时存在
         * joins 若存在,则tables 必须存在
         * @param defaultClassInfo field不带表名时,使用此默认信息
         */
        public Object[] build(EntityContainer.EntityClassInfo defaultClassInfo, String leftDecorated, String rightDecorated) {
            StringBuilder sb = new StringBuilder();
            List<Object> parameters = new ArrayList<>();
            // select 构建
            buildSelect(sb, defaultClassInfo, leftDecorated, rightDecorated);
            // from 构建
            buildFrom(sb, leftDecorated, rightDecorated);
            // join 构建
            buildJoin(sb, leftDecorated, rightDecorated);
            // where 构建
            buildWhere(sb, parameters, defaultClassInfo, leftDecorated, rightDecorated);
            // order 构建
            buildOrder(sb, defaultClassInfo, leftDecorated, rightDecorated);

            return new Object[]{sb.toString(), parameters};
        }

        // select 构建
        public String buildSelect(StringBuilder sb, EntityContainer.EntityClassInfo defaultClassInfo,
                                  String leftDecorated, String rightDecorated) {
            if (!selectFields.isEmpty()) {
                StringBuilder selection = new StringBuilder(" SELECT ");
                selectFields.forEach(field -> {
                    String[] fv = parseField(defaultClassInfo, field);
                    if (!"".equals(fv[0])) {
                        selection.append(leftDecorated).append(fv[0]).append(rightDecorated).append(POINT);
                    }
                    selection.append(leftDecorated).append(fv[1]).append(rightDecorated).append(", ");
                });
                if (sb != null) {
                    sb.append(selection.substring(0, selection.lastIndexOf(", ")));
                }
                return selection.substring(0, selection.lastIndexOf(", "));
            }
            return null;
        }

        // from 构建
        public String buildFrom(StringBuilder sb, String leftDecorated, String rightDecorated) {
            if (!tables.isEmpty()) {
                StringBuilder from = new StringBuilder(" FROM ");
                tables.forEach(table -> {
                    EntityContainer.EntityClassInfo classInfo = EntityContainer.getEntityClassByClazz(table);
                    if (classInfo != null) {
                        from.append(leftDecorated).append(classInfo.tableName).append(rightDecorated).append(", ");
                    } else {
                        logger.info("can't find the table that named {}", table);
                    }
                });
                if (sb != null) {
                    sb.append(from.substring(0, from.lastIndexOf(", ")));
                }
                return from.substring(0, from.lastIndexOf(", "));
            }
            return null;
        }

        // join 构建
        public String buildJoin(StringBuilder sb, String leftDecorated, String rightDecorated) {
            if (!joins.isEmpty()) {
                StringBuilder join = new StringBuilder();
                joins.forEach(item -> {
                    EntityContainer.EntityClassInfo classInfo = EntityContainer.getEntityClassByClazz(item.table);
                    if (classInfo != null) {
                        join.append(" ").append(item.op.toString()).append(" JOIN ").append(leftDecorated)
                                .append(classInfo.tableName).append(rightDecorated).append(" ON ")
                                .append(item.condition) // TODO: 2017/10/11 condition need to be parsed
                                .append(", ");
                    } else {
                        logger.info("can't find the table that named {}", item.table);
                    }
                });
                if (sb != null) {
                    sb.append(join.substring(0, join.lastIndexOf(", ")));
                }
                return join.substring(0, join.lastIndexOf(", "));
            }
            return null;
        }

        // where 构建
        public Object[] buildWhere(StringBuilder sb, List<Object> parameters, EntityContainer.EntityClassInfo defaultClassInfo,
                                   String leftDecorated, String rightDecorated) {
            if (!conditions.isEmpty()) {
                StringBuilder condition = new StringBuilder();
                List<Object> values = new ArrayList<>();
                conditions.forEach(cond -> {
                    String[] fv = parseField(defaultClassInfo, cond.field1);
                    String f = "";
                    if (!"".equals(fv[0])) {
                        f += leftDecorated + fv[0] + rightDecorated + POINT;
                    }
                    f += leftDecorated + fv[1] + rightDecorated;
                    switch (cond.op) {
                        case EQUAL:
                            // 联表条件处理
                            if (cond.field2 != null && cond.value1 == null) {
                                String[] fv2 = parseField(null, cond.field2);
                                // 联表查询必须传带表名的field,这里认为能找到对应的表和列,否则生成错误SQL
                                String f2 = leftDecorated + fv2[0] + rightDecorated + POINT
                                        + leftDecorated + fv2[1] + rightDecorated;
                                condition.append(" AND ").append(f).append(" = ").append(f2);
                            } else {
                                condition.append(" AND " + f + " = ?");
                                values.add(cond.value1);
                            }
                            break;
                        case NOT_EQUAL:
                            condition.append(" AND " + f + " != ?");
                            values.add(cond.value1);
                            break;
                        case GT:
                            condition.append(" AND " + f + " > ?");
                            values.add(cond.value1);
                            break;
                        case GE:
                            condition.append(" AND " + f + " >= ?");
                            values.add(cond.value1);
                            break;
                        case LT:
                            condition.append(" AND " + f + " < ?");
                            values.add(cond.value1);
                            break;
                        case LE:
                            condition.append(" AND " + f + " <= ?");
                            values.add(cond.value1);
                            break;
                        case LIKE:
                            condition.append(" AND " + f + " LIKE ?");
                            values.add(cond.value1);
                            break;
                        case IN:
                            condition.append(" AND " + f + " IN " + ((List) cond.value1).stream()
                                    .map(o -> "?")
                                    .collect(Collectors.joining(",", "(", ")")));
                            values.add(cond.value1);
                            break;
                        case NOT_IN:
                            condition.append(" AND " + f + " NOT IN " + ((List) cond.value1).stream()
                                    .map(o -> "?")
                                    .collect(Collectors.joining(",", "(", ")")));
                            values.add(cond.value1);
                            break;
                        case IS_NULL:
                            condition.append(" AND " + f + " IS NULL");
                            break;
                        case NOT_NULL:
                            condition.append(" AND " + f + " IS NOT NULL");
                            break;
                        case BETWEEN:
                            condition.append(" AND " + f + " BETWEEN ? AND ?");
                            values.add(cond.value1);
                            values.add(cond.value2);
                            break;
                    }
                });
                StringBuilder where = new StringBuilder();
                if (condition.length() > 0) {
                    where.append(" WHERE ");
                    // 去掉前面多余的 " AND "
                    where.append(condition.toString().substring(5));
                }
                if (sb != null) {
                    sb.append(where);
                    parameters.addAll(values);
                }
                return new Object[]{where.toString(), values};
            }
            return new Object[]{};
        }

        // order 构建
        public String buildOrder(StringBuilder sb, EntityContainer.EntityClassInfo defaultClassInfo,
                                 String leftDecorated, String rightDecorated) {
            if (!order.isEmpty()) {
                StringBuilder o = new StringBuilder();
                o.append(" ORDER BY ");
                order.forEach((key, value) -> {
                    String[] fv = parseField(defaultClassInfo, key);
                    String f = "";
                    if (!"".equals(fv[0])) {
                        f += leftDecorated + fv[0] + rightDecorated + POINT;
                    }
                    f += leftDecorated + fv[1] + rightDecorated;
                    o.append(f).append(" ").append(value ? "ASC" : "DESC").append(", ");
                });
                if (sb != null) {
                    sb.append(o.substring(0, o.lastIndexOf(", ")));
                }
                return o.substring(0, o.lastIndexOf(", "));
            }
            return null;
        }

        public SB select(String... fields) {
            for (int i = 0; i < fields.length; i++) {
                selectFields.add(fields[i]);
            }
            return this;
        }

        public SB from(String... tables) {
            for (int i = 0; i < tables.length; i++) {
                this.tables.add(tables[i]);
            }
            return this;
        }

        public SB leftJoin(String table, String condition) {
            joins.add(new SQLJoin(SQLJoin.OP.LEFT, table, condition));
            return this;
        }

        /**
         * 解析Field,将类名.属性名解析成表名.列表
         * @param field class.field
         * @return [table,column]
         */
        private String[] parseField(EntityContainer.EntityClassInfo defaultClassInfo, String field) {
            String table = "";
            String column = field;
            if (field.indexOf(POINT) > 0) {
                String[] sv = field.split("\\.");
                table = sv[0];
                column = sv[1];
            }
            // 传入field带有表名,以此优先
            if (!"".equals(table)) {
                EntityContainer.EntityClassInfo classInfo = EntityContainer.getEntityClassByClazz(table);
                // 如果为空,说明传入不正确
                if (classInfo != null) {
                    table = classInfo.tableName;
                    // it will throw null point exception when the column name is error
                    if (!"*".equals(column)) {
                        column = classInfo.columns.get(column).columnName;
                    }
                }
            } else if (defaultClassInfo != null) {
                table = defaultClassInfo.tableName;
                // it will throw null point exception when the column name is error
                column = defaultClassInfo.columns.get(column).columnName;
            }
            return new String[]{table, column};
        }

        // 字段条件 table.field = ?
        public SB eqVL(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.EQUAL, field, null, value, null));
            return this;
        }

        // 联表条件相等 table1.field = table2.field
        public SB eqFD(String field1, String field2) {
            conditions.add(new SQLCondition(SQLCondition.OP.EQUAL, field1, field2, null, null));
            return this;
        }

        public SB notEq(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.NOT_EQUAL, field, null, value, null));
            return this;
        }

        public SB gt(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.GT, field, null, value, null));
            return this;
        }

        public SB ge(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.GE, field, null, value, null));
            return this;
        }

        public SB lt(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.LT, field, null, value, null));
            return this;
        }

        public SB le(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.LE, field, null, value, null));
            return this;
        }

        public SB like(String field, Object value) {
            conditions.add(new SQLCondition(SQLCondition.OP.LIKE, field, null, value, null));
            return this;
        }

        public SB in(String field, List<Object> values) {
            conditions.add(new SQLCondition(SQLCondition.OP.IN, field, null, values, null));
            return this;
        }

        public SB notIn(String field, List<Object> values) {
            conditions.add(new SQLCondition(SQLCondition.OP.NOT_IN, field, null,values, null));
            return this;
        }

        public SB isNull(String field) {
            conditions.add(new SQLCondition(SQLCondition.OP.IS_NULL, field, null, null, null));
            return this;
        }

        public SB notNull(String field) {
            conditions.add(new SQLCondition(SQLCondition.OP.NOT_NULL, field, null, null, null));
            return this;
        }

        public SB between(String field, Object value1, Object value2) {
            conditions.add(new SQLCondition(SQLCondition.OP.BETWEEN, field, null, value1, value2));
            return this;
        }

        public SB asc(String field) {
            order.put(field, true);
            return this;
        }

        public SB desc(String field) {
            order.put(field, false);
            return this;
        }

        private static class SQLJoin {
            private OP op;
            private String table;
            private String condition;

            public SQLJoin(OP op, String table, String condition) {
                this.op = op;
                this.table = table;
                this.condition = condition;
            }

            enum OP {
                LEFT, RIGHT, INNER
            }
        }

        private static class SQLCondition {

            private OP op;
            private String field1;
            private String field2;
            private Object value1;
            private Object value2;

            SQLCondition(OP op, String field1, String field2, Object value1, Object value2) {
                this.op = op;
                this.field1 = field1;
                this.field2 = field2;
                this.value1 = value1;
                this.value2 = value2;
            }

            enum OP {
                EQUAL, NOT_EQUAL, GT, GE, LT, LE, IN, NOT_IN, LIKE, IS_NULL, NOT_NULL, BETWEEN
            }
        }
    }


}

