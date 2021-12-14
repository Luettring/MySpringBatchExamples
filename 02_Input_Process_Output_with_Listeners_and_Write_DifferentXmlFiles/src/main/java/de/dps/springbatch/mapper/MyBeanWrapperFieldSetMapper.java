package de.dps.springbatch.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyBeanWrapperFieldSetMapper<Person> extends BeanWrapperFieldSetMapper<Person> {

    @Override
    protected void initBinder(DataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isNotEmpty(text) && !StringUtils.isAlphaSpace(text)) {
                    setValue(LocalDate.parse(text, formatter));
                }
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                Object date = getValue();
                if (date != null) {
                    return formatter.format((LocalDate) date);
                } else {
                    return "";
                }
            }
        });
    }
}