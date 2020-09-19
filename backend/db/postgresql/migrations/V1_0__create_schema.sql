--
-- PostgreSQL database dump
--
-- Dumped from database version 12.3
-- Dumped by pg_dump version 12.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET default_table_access_method = heap;

--
-- User data within our system
--
CREATE TABLE public.app_user (
    id bigint NOT NULL,
    email character varying(255),
    name character varying(255),
    oauth_user_id character varying(255) NOT NULL
);
ALTER TABLE public.app_user OWNER TO ${dbAdmin};

--
-- Bookmark data
--
CREATE TABLE public.bookmark (
    id bigint NOT NULL,
    archived_date_time timestamp without time zone,
    created_date_time timestamp without time zone NOT NULL,
    description text,
    read_status integer,
    shared_status integer,
    title character varying(255),
    updated_date_time timestamp without time zone NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    owner_id bigint NOT NULL,
    url_id bigint
);
ALTER TABLE public.bookmark OWNER TO ${dbAdmin};

--
-- Hibernate managed sequence
--
CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.hibernate_sequence
    OWNER TO ${dbAdmin};

--
-- Tags belonging to a Bookmark *->1
--
CREATE TABLE public.tag (
    id character varying(255) NOT NULL,
    pos integer NOT NULL,
    bookmark_id bigint NOT NULL
);
ALTER TABLE public.tag OWNER TO ${dbAdmin};

--
-- Mapping user to Role
--
CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id character varying(3)
);
ALTER TABLE public.user_role OWNER TO ${dbAdmin};

--
-- Url that a Bookmark references 1->*
--
CREATE TABLE public.web_url (
    id bigint NOT NULL,
    created_date_time timestamp without time zone NOT NULL,
    url text NOT NULL
);
ALTER TABLE public.web_url OWNER TO ${dbAdmin};

--
-- app_user primary key
--
ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);

--
-- bookmark primary key
--
ALTER TABLE ONLY public.bookmark
    ADD CONSTRAINT bookmark_pkey PRIMARY KEY (id);

--
-- Tag name is it's id, and unique to id+bookmark 
--
ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (bookmark_id, id);

--
-- user to role constraint
--
ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT uk872xec3woupu3gw59b04pj3sa UNIQUE (user_id, role_id);

--
-- User's oauth id is unique within our system
--
ALTER TABLE ONLY public.app_user
  ADD CONSTRAINT uk_hauf8ey208tip2iawxk03v8uq UNIQUE (oauth_user_id);

--
-- User name is unique within our system
--
ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk_ca699byqpy39i3fkohu5ya69m UNIQUE (name);

--
-- web_url are unique by url
--
ALTER TABLE ONLY public.web_url
    ADD CONSTRAINT uk_i5xa3pw28mrpylg5pjj1ahf6v UNIQUE (url);

--
-- User can only Bookmark a url once
--
ALTER TABLE ONLY public.bookmark
    ADD CONSTRAINT ukigmydl6cq8h8qlncs38ql2f87 UNIQUE (owner_id, url_id);

--
-- web_url primary key
--
ALTER TABLE ONLY public.web_url
    ADD CONSTRAINT web_url_pkey PRIMARY KEY (id);

CREATE INDEX idxit77eq964jhfqtu54081ebtio ON public.user_role USING btree (role_id);

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT fkc3vgdksjpcig43on7iiavqug9 FOREIGN KEY (bookmark_id) REFERENCES public.bookmark(id);

ALTER TABLE ONLY public.bookmark
    ADD CONSTRAINT fkd41x250cdqkchu8500u622075 FOREIGN KEY (owner_id) REFERENCES public.app_user(id);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkg7fr1r7o0fkk41nfhnjdyqn7b FOREIGN KEY (user_id) REFERENCES public.app_user(id);

ALTER TABLE ONLY public.bookmark
    ADD CONSTRAINT fkpx8h6itwt9l9jwug28jukg1lg FOREIGN KEY (url_id) REFERENCES public.web_url(id);



GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO ${dbUser};
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO ${dbUser};