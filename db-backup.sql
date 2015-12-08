--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: stock; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stock (
    id character(8) NOT NULL,
    icf_level integer DEFAULT 0 NOT NULL,
    ths_percent integer DEFAULT 0 NOT NULL,
    c_date date DEFAULT ('now'::text)::date NOT NULL,
    ths_date date
);


ALTER TABLE stock OWNER TO postgres;

--
-- Name: COLUMN stock.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN stock.id IS 'stock id';


--
-- Name: COLUMN stock.icf_level; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN stock.icf_level IS 'icaifu level';


--
-- Name: COLUMN stock.c_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN stock.c_date IS 'claw/created date';


--
-- Name: COLUMN stock.ths_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN stock.ths_date IS 'tonghuashun cacluated date';


--
-- Name: stock_base; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stock_base (
    id character(8) NOT NULL,
    name character varying,
    industry character varying
);


ALTER TABLE stock_base OWNER TO postgres;

--
-- Name: stock_closed; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stock_closed (
    id character(8) NOT NULL
);


ALTER TABLE stock_closed OWNER TO postgres;

--
-- Name: stock_error; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stock_error (
    id character(8) NOT NULL,
    ec_icf bigint DEFAULT 0,
    ec_ths bigint DEFAULT 0
);


ALTER TABLE stock_error OWNER TO postgres;

--
-- Name: stock_base_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stock_base
    ADD CONSTRAINT stock_base_pk PRIMARY KEY (id);


--
-- Name: stock_closed_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stock_closed
    ADD CONSTRAINT stock_closed_pk PRIMARY KEY (id);


--
-- Name: stock_error_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stock_error
    ADD CONSTRAINT stock_error_pk PRIMARY KEY (id);


--
-- Name: stock_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stock
    ADD CONSTRAINT stock_pk PRIMARY KEY (id, c_date);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

