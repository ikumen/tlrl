/**
 * 
 */
package com.gnoht.tlrl.bookmark.converters;

import org.springframework.core.convert.converter.Converter;

import com.gnoht.tlrl.bookmark.ReadStatus;

/**
 * @author ikumen
 */
public class ReadStatusConverter implements Converter<String, ReadStatus>{

  @Override
  public ReadStatus convert(String s) {
    return ReadStatus.valueOf(s.toUpperCase());
  }
}
