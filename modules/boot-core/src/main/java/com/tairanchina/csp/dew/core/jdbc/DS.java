package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.StandardCode;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.entity.EntityContainer;
import com.tairanchina.csp.dew.core.jdbc.dialect.Dialect;
import com.tairanchina.csp.dew.core.jdbc.dialect.DialectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class DS {

    private JdbcTemplate jdbcTemplate;
    private String jdbcUrl;

    private Dialect dialect;

    private void init() {
        dialect = DialectFactory.parseDialect(jdbcUrl);
    }

    public JdbcTemplate jdbc() {
        return jdbcTemplate;
    }

    @Transactional
    public long insert(Object entity) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entity.getClass());
        Object[] packageInsert = packageInsert(new ArrayList<Object>() {{
            add(entity);
        }}, true);
        jdbcTemplate.update((String) packageInsert[0], ((List<Object[]>) packageInsert[1]).get(0));
        if (entityClassInfo.pkFieldNameOpt.isPresent()) {
            // Has private key , return generated key
            // TODO use http://docs.spring.io/spring/docs/3.0.x/reference/jdbcTemplate.html#jdbc-auto-genereted-keys
            return jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);
        } else {
            return 0;
        }
    }

    @Transactional
    public void insert(Iterable<?> entities) {
        Object[] packageInsert = packageInsert(entities, false);
        jdbcTemplate.batchUpdate((String) packageInsert[0], (List<Object[]>) packageInsert[1]);
    }

    @Transactional
    public void updateById(long id, Object entity) {
        try {
            $.bean.setValue(entity, EntityContainer.getCodeFieldNameByClazz(entity.getClass()).pkFieldNameOpt.get(), id);
            Object[] packageUpdate = packageUpdate(entity, true);
            jdbcTemplate.update((String) packageUpdate[0], (Object[]) packageUpdate[1]);
        } catch (NoSuchFieldException e) {
            Dew.e(StandardCode.INTERNAL_SERVER_ERROR.toString(), e);
        }
    }

    @Transactional
    public void updateByCode(String code, Object entity) {
        try {
            $.bean.setValue(entity, EntityContainer.getCodeFieldNameByClazz(entity.getClass()).codeFieldNameOpt.get(), code);
            Object[] packageUpdate = packageUpdate(entity, true);
            jdbcTemplate.update((String) packageUpdate[0], (Object[]) packageUpdate[1]);
        } catch (NoSuchFieldException e) {
            Dew.e(StandardCode.INTERNAL_SERVER_ERROR.toString(), e);
        }
    }

    public <E> E getById(long id, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        Object[] packageSelect = packageSelect(entityClazz, new LinkedHashMap<String, Object>() {{
            put(entityClassInfo.pkFieldNameOpt.get(), id);
        }}, null);
        return convertRsToObj(jdbcTemplate.queryForMap((String) packageSelect[0], (Object[]) packageSelect[1]), entityClazz);
    }

    public <E> E getByCode(String code, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        Object[] packageSelect = packageSelect(entityClazz, new LinkedHashMap<String, Object>() {{
            put(entityClassInfo.codeFieldNameOpt.get(), code);
        }}, null);
        return convertRsToObj(jdbcTemplate.queryForMap((String) packageSelect[0], (Object[]) packageSelect[1]), entityClazz);
    }

    @Transactional
    public void deleteById(long id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("DELETE FROM %s WHERE `%s` = ?",
                entityClassInfo.tableName, entityClassInfo.columns.get(entityClassInfo.pkFieldNameOpt.get()).columnName),
                id);
    }

    @Transactional
    public void deleteByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("DELETE FROM %s WHERE `%s` = ?",
                entityClassInfo.tableName, entityClassInfo.columns.get(entityClassInfo.codeFieldNameOpt.get()).columnName),
                code);
    }

    @Transactional
    public void enableById(long id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("UPDATE %s SET `%s` = ? WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName,
                entityClassInfo.columns.get(entityClassInfo.pkFieldNameOpt.get()).columnName),
                true, id);
    }

    @Transactional
    public void enableByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("UPDATE %s SET `%s` = ? WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName,
                entityClassInfo.columns.get(entityClassInfo.codeFieldNameOpt.get()).columnName),
                true, code);
    }

    @Transactional
    public void disableById(long id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("UPDATE %s SET `%s` = ? WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName,
                entityClassInfo.columns.get(entityClassInfo.pkFieldNameOpt.get()).columnName),
                false, id);
    }

    @Transactional
    public void disableByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        jdbcTemplate.update(String.format("UPDATE %s SET `%s` = ? WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName,
                entityClassInfo.columns.get(entityClassInfo.codeFieldNameOpt.get()).columnName),
                false, code);
    }

    public boolean existById(long id, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM %s WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.pkFieldNameOpt.get()).columnName),
                new Object[]{id}, Long.class) != 0;
    }

    public boolean existByCode(String code, Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM %s WHERE `%s` = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.codeFieldNameOpt.get()).columnName),
                new Object[]{code}, Long.class) != 0;
    }

    public <E> List<E> findAll(Class<E> entityClazz) {
        return find(null, null, entityClazz);
    }

    public <E> List<E> findAll(LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return find(null, orderDesc, entityClazz);
    }

    public <E> List<E> findEnabled(Class<E> entityClazz) {
        return find(true, null, entityClazz);
    }


    public <E> List<E> findEnabled(LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return find(true, orderDesc, entityClazz);
    }

    public <E> List<E> findDisabled(Class<E> entityClazz) {
        return find(false, null, entityClazz);
    }

    public <E> List<E> findDisabled(LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return find(false, orderDesc, entityClazz);
    }

    private <E> List<E> find(Boolean enable, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        LinkedHashMap where = new LinkedHashMap<>();
        if (enable != null) {
            where.put(entityClassInfo.enabledFieldNameOpt.get(), enable);
        }
        Object[] packageSelect = packageSelect(entityClazz, where, orderDesc);
        return jdbcTemplate.queryForList((String) packageSelect[0], (Object[]) packageSelect[1]).stream()
                .map(row -> convertRsToObj(row, entityClazz))
                .collect(Collectors.toList());
    }

    public long countAll(Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM %s",
                entityClassInfo.tableName),
                new Object[]{}, Long.class);
    }

    public long countEnabled(Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM %s WHERE %s = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName),
                new Object[]{true}, Long.class);
    }

    public long countDisabled(Class<?> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        return jdbcTemplate.queryForObject(String.format("SELECT COUNT(1) FROM %s WHERE %s = ?",
                entityClassInfo.tableName,
                entityClassInfo.columns.get(entityClassInfo.enabledFieldNameOpt.get()).columnName),
                new Object[]{false}, Long.class);
    }

    public <E> Page<E> paging(long pageNumber, int pageSize, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, null, null, entityClazz);
    }

    public <E> Page<E> paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, null, orderDesc, entityClazz);
    }

    public <E> Page<E> pagingEnabled(int pageNumber, int pageSize, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, true, null, entityClazz);
    }

    public <E> Page<E> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, true, orderDesc, entityClazz);
    }

    public <E> Page<E> pagingDisabled(int pageNumber, int pageSize, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, false, null, entityClazz);
    }

    public <E> Page<E> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        return paging(pageNumber, pageSize, false, orderDesc, entityClazz);
    }

    private <E> Page<E> paging(long pageNumber, int pageSize, Boolean enable, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        LinkedHashMap where = new LinkedHashMap<>();
        if (enable != null) {
            where.put(entityClassInfo.enabledFieldNameOpt.get(), enable);
        }
        Object[] packageSelect = packageSelect(entityClazz, where, orderDesc);
        String countSql = wrapCountSql((String) packageSelect[0]);
        String pagedSql = wrapPagingSql((String) packageSelect[0], pageNumber, pageSize);
        long totalRecords = jdbcTemplate.queryForObject(countSql, (Object[]) packageSelect[1], Long.class);
        List<E> objects = jdbcTemplate.queryForList(pagedSql, (Object[]) packageSelect[1]).stream()
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
     * @return 格式 Object[]{Sql:String,params:List<Object[]>}
     */
    private Object[] packageInsert(Iterable<?> entities, boolean ignoreNullValue) {
        if (!entities.iterator().hasNext()) {
            throw Dew.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Entity List is empty."));
        }
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entities.iterator().next().getClass());
        String sql = null;
        List<Object[]> params = new ArrayList<>();
        for (Object entity : entities) {
            Map<String, Object> values = $.bean.findValues(entity, null, null, entityClassInfo.columns.keySet(), null);
            // Remove private key field
            if (entityClassInfo.pkFieldNameOpt.isPresent() && values.containsKey(entityClassInfo.pkFieldNameOpt.get())) {
                values.remove(entityClassInfo.pkFieldNameOpt.get());
            }
            // Add ext values
            if (entityClassInfo.codeFieldNameOpt.isPresent() && entityClassInfo.codeUUIDOpt.get() &&
                    (!values.containsKey(entityClassInfo.codeFieldNameOpt.get()) ||
                            values.get(entityClassInfo.codeFieldNameOpt.get()) == null ||
                            values.get(entityClassInfo.codeFieldNameOpt.get()).toString().isEmpty())) {
                values.put(entityClassInfo.codeFieldNameOpt.get(), $.field.createUUID());
            }
            Date now = new Date();
            if (entityClassInfo.createUserFieldNameOpt.isPresent()) {
                if (Dew.context().optInfo().isPresent()) {
                    values.put(entityClassInfo.createUserFieldNameOpt.get(), Dew.context().optInfo().get().getAccountCode());
                } else {
                    values.put(entityClassInfo.createUserFieldNameOpt.get(), "");
                }
            }
            if (entityClassInfo.createTimeFieldNameOpt.isPresent()) {
                values.put(entityClassInfo.createTimeFieldNameOpt.get(), now);
            }
            if (entityClassInfo.updateUserFieldNameOpt.isPresent()) {
                if (Dew.context().optInfo().isPresent()) {
                    values.put(entityClassInfo.updateUserFieldNameOpt.get(), Dew.context().optInfo().get().getAccountCode());
                } else {
                    values.put(entityClassInfo.updateUserFieldNameOpt.get(), "");
                }
            }
            if (entityClassInfo.updateTimeFieldNameOpt.isPresent()) {
                values.put(entityClassInfo.updateTimeFieldNameOpt.get(), now);
            }
            // Check null
            if (values.entrySet().stream()
                    .anyMatch(entry -> entityClassInfo.columns.get(entry.getKey()).notNull && entry.getValue() == null)) {
                throw Dew.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Not Null check fail."));
            }
            // Filter null value if ignoreNullValue=true
            if (ignoreNullValue) {
                values = values.entrySet().stream()
                        .filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            if (sql == null) {
                // Package
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO ").append(entityClassInfo.tableName);
                sb.append(values.entrySet().stream()
                        .map(entry -> "`" + entityClassInfo.columns.get(entry.getKey()).columnName + "`")
                        .collect(Collectors.joining(", ", " (", ") ")));
                sb.append("VALUES");
                sb.append(values.keySet().stream().map(o -> "?").collect(Collectors.joining(", ", " (", ") ")));
                sql = sb.toString();
            }
            params.add(values.values().toArray());
        }
        return new Object[]{sql, params};
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
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entity.getClass());
        Map<String, Object> values = $.bean.findValues(entity, null, null, entityClassInfo.columns.keySet(), null);
        // Check id or code Not empty
        if (!entityClassInfo.pkFieldNameOpt.isPresent() && !entityClassInfo.codeFieldNameOpt.isPresent()) {
            throw Dew.e(StandardCode.NOT_FOUND.toString(), new RuntimeException("Need @PkColumn or @CodeColumn field."));
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
                throw Dew.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Need Private Key or Code value."));
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
                values.put(entityClassInfo.updateUserFieldNameOpt.get(), "");
            }
        }
        if (entityClassInfo.updateTimeFieldNameOpt.isPresent()) {
            values.put(entityClassInfo.updateTimeFieldNameOpt.get(), new Date());
        }
        // Check null
        if (values.entrySet().stream()
                .anyMatch(entry -> entityClassInfo.columns.get(entry.getKey()).notNull && entry.getValue() == null)) {
            throw Dew.e(StandardCode.BAD_REQUEST.toString(), new RuntimeException("Not Null check fail."));
        }
        // Package
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sb.append("UPDATE ").append(entityClassInfo.tableName).append(" SET ");
        sb.append(values.entrySet().stream()
                .map(entry -> {
                    params.add(entry.getValue());
                    return "`" + entityClassInfo.columns.get(entry.getKey()).columnName + "` = ?";
                })
                .collect(Collectors.joining(", ")));
        sb.append(String.format(" WHERE `%s` = ?", whereColumnName));
        params.add(whereValue);
        return new Object[]{sb.toString(), params.toArray()};
    }

    /**
     * 组装查询SQL
     *
     * @param entityClazz 实体类型
     * @param where       查询条件 fieldName -> value , 确保有序，用于查询优化
     * @param orderDesc   排序条件 fieldName -> 是否降序 , 确保有序
     * @return 格式 Object[]{Sql:String,params:Object[]}
     */
    private Object[] packageSelect(Class<?> entityClazz, LinkedHashMap<String, Object> where, LinkedHashMap<String, Boolean> orderDesc) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        StringBuilder sb = new StringBuilder();
        Object[] params = new Object[]{};
        sb.append("SELECT ");
        sb.append(entityClassInfo.columns.values().stream().map(col -> "`" + col.columnName + "`").collect(Collectors.joining(", ")));
        sb.append(" FROM ").append(entityClassInfo.tableName);
        if (where != null && !where.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(where.entrySet().stream().map(col -> "`" + entityClassInfo.columns.get(col.getKey()).columnName + "` = ? ").collect(Collectors.joining("AND")));
            params = where.values().toArray();
        }
        if (orderDesc != null && !orderDesc.isEmpty()) {
            sb.append(" ORDER BY ");
            sb.append(orderDesc.entrySet().stream().map(col -> "`" + entityClassInfo.columns.get(col.getKey()).columnName + "` " + (col.getValue() ? "DESC" : "ASC")).collect(Collectors.joining(" ")));
        }
        return new Object[]{sb.toString(), params};
    }

    private <E> E convertRsToObj(Map<String, Object> rs, Class<E> entityClazz) {
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getCodeFieldNameByClazz(entityClazz);
        try {
            E entity = entityClazz.newInstance();
            for (Map.Entry<String, Object> entry : rs.entrySet()) {
                $.bean.setValue(entity, entityClassInfo.columnRel.get(entry.getKey().toLowerCase()), entry.getValue());
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            Dew.e(StandardCode.INTERNAL_SERVER_ERROR.toString(), e);
            return null;
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
}

