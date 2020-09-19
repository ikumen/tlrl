-- new oauth_user_id column tracks the provider + id for each user.
ALTER TABLE public.app_user
  ADD COLUMN oauth_user_id character varying(255) NOT NULL;

ALTER TABLE ONLY public.app_user
  ADD CONSTRAINT uk_hauf8ey208tip2iawxk03v8uq UNIQUE (oauth_user_id);
