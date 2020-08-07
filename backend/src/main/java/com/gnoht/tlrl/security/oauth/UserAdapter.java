/**
 * 
 */
package com.gnoht.tlrl.security.oauth;

import com.gnoht.tlrl.user.User;

/**
 * An adapter that conforms to our {@link User} and an external user/principal type.
 * 
 * @author ikumen
 */
public interface UserAdapter<T, E> {

  /**
   * @return the {@link User} wrapped by this adapter.
   */
  User getUser();
  
  /**
   * @return the external type wrapped by this adapter.
   */
  E getExternalUser(); 
  
  /**
   * Return a new {@link UserAdapter} with the current User and given external user.
   * 
   * @param extUser
   * @return
   */
  T withExternal(E extUser);
  
  /**
   * Return a new {@link UserAdapter} with the current external user and given user.
   * 
   * @param user
   * @return
   */
  T withUser(User user);
}
