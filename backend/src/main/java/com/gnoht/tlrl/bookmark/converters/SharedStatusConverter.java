/**
 * 
 */
package com.gnoht.tlrl.bookmark.converters;

import org.springframework.core.convert.converter.Converter;

import com.gnoht.tlrl.bookmark.SharedStatus;

/**
 * @author ikumen
 */
public class SharedStatusConverter implements Converter<String, SharedStatus>{

  @Override
  public SharedStatus convert(String source) {
    return SharedStatus.valueOf(source.toUpperCase());
  }

}
