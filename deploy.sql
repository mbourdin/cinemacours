--
-- PostgreSQL database dump
--

-- Dumped from database version 10.6 (Debian 10.6-1.pgdg90+1)
-- Dumped by pg_dump version 10.6 (Debian 10.6-1.pgdg90+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: billets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.billets (
    id bigint NOT NULL,
    prix double precision,
    commande_id bigint,
    seance_id bigint
);


ALTER TABLE public.billets OWNER TO postgres;

--
-- Name: billets_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.billets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.billets_id_seq OWNER TO postgres;

--
-- Name: billets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.billets_id_seq OWNED BY public.billets.id;


--
-- Name: commande; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commande (
    id bigint NOT NULL,
    date timestamp without time zone,
    paye boolean,
    person_id bigint
);


ALTER TABLE public.commande OWNER TO postgres;

--
-- Name: commande_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commande_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.commande_id_seq OWNER TO postgres;

--
-- Name: commande_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commande_id_seq OWNED BY public.commande.id;


--
-- Name: film_genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.film_genre (
    genre_id bigint NOT NULL,
    film_id integer NOT NULL
);


ALTER TABLE public.film_genre OWNER TO postgres;

--
-- Name: films; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.films (
    id bigint NOT NULL,
    title character varying(255),
    rating numeric(2,1),
    image_path character varying(120),
    summary text,
    film_director bigint,
    release_date date,
    duree integer,
    tmbd_id bigint,
    titre_original character varying(255)
);


ALTER TABLE public.films OWNER TO postgres;

--
-- Name: films_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.films_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.films_id_seq OWNER TO postgres;

--
-- Name: films_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.films_id_seq OWNED BY public.films.id;


--
-- Name: genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genres (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    tmbd_id bigint
);


ALTER TABLE public.genres OWNER TO postgres;

--
-- Name: genres_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genres_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genres_id_seq OWNER TO postgres;

--
-- Name: genres_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genres_id_seq OWNED BY public.genres.id;


--
-- Name: message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message (
    id integer NOT NULL,
    date date NOT NULL,
    email character varying(255),
    lu boolean,
    texte character varying(255) NOT NULL
);


ALTER TABLE public.message OWNER TO postgres;

--
-- Name: message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.message_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.message_id_seq OWNER TO postgres;

--
-- Name: message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.message_id_seq OWNED BY public.message.id;


--
-- Name: news; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.news (
    id integer NOT NULL,
    date date NOT NULL,
    texte character varying(255) NOT NULL
);


ALTER TABLE public.news OWNER TO postgres;

--
-- Name: news_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.news_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.news_id_seq OWNER TO postgres;

--
-- Name: news_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.news_id_seq OWNED BY public.news.id;


--
-- Name: persons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.persons (
    id bigint NOT NULL,
    image_path character varying(80),
    birthday date,
    name character varying(255),
    tmdb_id bigint
);


ALTER TABLE public.persons OWNER TO postgres;

--
-- Name: persons_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.persons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.persons_id_seq OWNER TO postgres;

--
-- Name: persons_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.persons_id_seq OWNED BY public.persons.id;


--
-- Name: play; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.play (
    id bigint NOT NULL,
    film_id bigint NOT NULL,
    person_id bigint NOT NULL,
    rank integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.play OWNER TO postgres;

--
-- Name: play_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.play_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.play_id_seq OWNER TO postgres;

--
-- Name: play_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.play_id_seq OWNED BY public.play.id;


--
-- Name: review; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.review (
    id bigint NOT NULL,
    article text NOT NULL,
    datte timestamp(6) without time zone NOT NULL,
    film_id bigint,
    user_id bigint,
    etat integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.review OWNER TO postgres;

--
-- Name: review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.review_id_seq OWNER TO postgres;

--
-- Name: review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.review_id_seq OWNED BY public.review.id;


--
-- Name: salle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salle (
    id integer NOT NULL,
    taille integer,
    active boolean DEFAULT false NOT NULL,
    nom character varying(255)
);


ALTER TABLE public.salle OWNER TO postgres;

--
-- Name: salle_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.salle_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.salle_id_seq OWNER TO postgres;

--
-- Name: salle_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.salle_id_seq OWNED BY public.salle.id;


--
-- Name: seance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seance (
    id bigint NOT NULL,
    debut timestamp without time zone NOT NULL,
    film_id bigint,
    salle_id integer,
    tarif_id integer
);


ALTER TABLE public.seance OWNER TO postgres;

--
-- Name: seance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seance_id_seq OWNER TO postgres;

--
-- Name: seance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.seance_id_seq OWNED BY public.seance.id;


--
-- Name: tarif; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tarif (
    id integer NOT NULL,
    normal double precision,
    reduit double precision,
    actif boolean DEFAULT true NOT NULL,
    nom character varying(255)
);


ALTER TABLE public.tarif OWNER TO postgres;

--
-- Name: tarif_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tarif_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tarif_id_seq OWNER TO postgres;

--
-- Name: tarif_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tarif_id_seq OWNED BY public.tarif.id;


--
-- Name: tmdb_films; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tmdb_films (
    id bigint NOT NULL,
    title character varying(255)
);


ALTER TABLE public.tmdb_films OWNER TO postgres;

--
-- Name: user_news; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_news (
    news_id integer NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.user_news OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    abonne boolean NOT NULL,
    email character varying(30) NOT NULL,
    password character varying(255),
    link character varying(80),
    login character varying(75) NOT NULL,
    surname character varying(255),
    givenname character varying(255),
    type integer DEFAULT 4 NOT NULL,
    actif boolean DEFAULT false
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: billets id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billets ALTER COLUMN id SET DEFAULT nextval('public.billets_id_seq'::regclass);


--
-- Name: commande id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande ALTER COLUMN id SET DEFAULT nextval('public.commande_id_seq'::regclass);


--
-- Name: films id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.films ALTER COLUMN id SET DEFAULT nextval('public.films_id_seq'::regclass);


--
-- Name: genres id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genres ALTER COLUMN id SET DEFAULT nextval('public.genres_id_seq'::regclass);


--
-- Name: message id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message ALTER COLUMN id SET DEFAULT nextval('public.message_id_seq'::regclass);


--
-- Name: news id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news ALTER COLUMN id SET DEFAULT nextval('public.news_id_seq'::regclass);


--
-- Name: persons id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.persons ALTER COLUMN id SET DEFAULT nextval('public.persons_id_seq'::regclass);


--
-- Name: play id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play ALTER COLUMN id SET DEFAULT nextval('public.play_id_seq'::regclass);


--
-- Name: review id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review ALTER COLUMN id SET DEFAULT nextval('public.review_id_seq'::regclass);


--
-- Name: salle id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salle ALTER COLUMN id SET DEFAULT nextval('public.salle_id_seq'::regclass);


--
-- Name: seance id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seance ALTER COLUMN id SET DEFAULT nextval('public.seance_id_seq'::regclass);


--
-- Name: tarif id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tarif ALTER COLUMN id SET DEFAULT nextval('public.tarif_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: billets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.billets (id, prix, commande_id, seance_id) FROM stdin;
\.


--
-- Data for Name: commande; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commande (id, date, paye, person_id) FROM stdin;
\.


--
-- Data for Name: film_genre; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.film_genre (genre_id, film_id) FROM stdin;
\.


--
-- Data for Name: films; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.films (id, title, rating, image_path, summary, film_director, release_date, duree, tmbd_id, titre_original) FROM stdin;
\.


--
-- Data for Name: genres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.genres (id, name, tmbd_id) FROM stdin;
33	Action	28
34	Science-Fiction	878
35	Aventure	12
36	Thriller	53
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (id, date, email, lu, texte) FROM stdin;
\.


--
-- Data for Name: news; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.news (id, date, texte) FROM stdin;
\.


--
-- Data for Name: persons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.persons (id, image_path, birthday, name, tmdb_id) FROM stdin;
\.


--
-- Data for Name: play; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.play (id, film_id, person_id, rank, name) FROM stdin;
\.


--
-- Data for Name: review; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.review (id, article, datte, film_id, user_id, etat) FROM stdin;
\.


--
-- Data for Name: salle; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.salle (id, taille, active, nom) FROM stdin;
1	40	t	0
3	50	t	1
4	55	t	2
2	45	t	3
\.


--
-- Data for Name: seance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seance (id, debut, film_id, salle_id, tarif_id) FROM stdin;
\.


--
-- Data for Name: tarif; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tarif (id, normal, reduit, actif, nom) FROM stdin;
\.


--
-- Data for Name: tmdb_films; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tmdb_films (id, title) FROM stdin;
\.


--
-- Data for Name: user_news; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_news (news_id, user_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, abonne, email, password, link, login, surname, givenname, type, actif) FROM stdin;
17	t	bourdin.maurice@gmail.com	$2a$10$y40/KQmSeatZRqtXzNpw4.4PfqMpebWnPoLI1pXV31dN1IJ0ULTmy		admin			13	t
\.


--
-- Name: billets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.billets_id_seq', 89, true);


--
-- Name: commande_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commande_id_seq', 31, true);


--
-- Name: films_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.films_id_seq', 51, true);


--
-- Name: genres_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.genres_id_seq', 36, true);


--
-- Name: message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_id_seq', 15, true);


--
-- Name: news_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.news_id_seq', 4, true);


--
-- Name: persons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.persons_id_seq', 1449, true);


--
-- Name: play_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.play_id_seq', 1962, true);


--
-- Name: review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.review_id_seq', 27, true);


--
-- Name: salle_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.salle_id_seq', 1, false);


--
-- Name: seance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seance_id_seq', 12, true);


--
-- Name: tarif_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tarif_id_seq', 5, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 20, true);


--
-- Name: billets billets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billets
    ADD CONSTRAINT billets_pkey PRIMARY KEY (id);


--
-- Name: commande commande_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande
    ADD CONSTRAINT commande_pkey PRIMARY KEY (id);


--
-- Name: film_genre film_genre_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_genre
    ADD CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id);


--
-- Name: films films_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.films
    ADD CONSTRAINT films_pkey PRIMARY KEY (id);


--
-- Name: genres genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genres
    ADD CONSTRAINT genre_pkey PRIMARY KEY (id);


--
-- Name: message message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: news news_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pkey PRIMARY KEY (id);


--
-- Name: persons persons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT persons_pkey PRIMARY KEY (id);


--
-- Name: play play_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play
    ADD CONSTRAINT play_pk UNIQUE (film_id, person_id, name);


--
-- Name: play play_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play
    ADD CONSTRAINT play_pkey PRIMARY KEY (id);


--
-- Name: review review_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT review_pkey PRIMARY KEY (id);


--
-- Name: salle salle_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salle
    ADD CONSTRAINT salle_pkey PRIMARY KEY (id);


--
-- Name: seance seance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seance
    ADD CONSTRAINT seance_pkey PRIMARY KEY (id);


--
-- Name: tarif tarif_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tarif
    ADD CONSTRAINT tarif_pkey PRIMARY KEY (id);


--
-- Name: tmdb_films tmdb_films_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tmdb_films
    ADD CONSTRAINT tmdb_films_pkey PRIMARY KEY (id);


--
-- Name: user_news user_news_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_news
    ADD CONSTRAINT user_news_pkey PRIMARY KEY (news_id, user_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: films_tmbd_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX films_tmbd_id_uindex ON public.films USING btree (tmbd_id);


--
-- Name: genre_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX genre_name_uindex ON public.genres USING btree (name);


--
-- Name: genres_tmbd_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX genres_tmbd_id_uindex ON public.genres USING btree (tmbd_id);


--
-- Name: persons_tmdb_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX persons_tmdb_id_uindex ON public.persons USING btree (tmdb_id);


--
-- Name: film_genre film_genre_films_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_genre
    ADD CONSTRAINT film_genre_films_id_fk FOREIGN KEY (film_id) REFERENCES public.films(id);


--
-- Name: film_genre film_genre_genre_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_genre
    ADD CONSTRAINT film_genre_genre_id_fk FOREIGN KEY (genre_id) REFERENCES public.genres(id);


--
-- Name: films films_persons_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.films
    ADD CONSTRAINT films_persons_id_fk FOREIGN KEY (film_director) REFERENCES public.persons(id);


--
-- Name: user_news fk3195dqlpc076xd2lra7dnx6gr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_news
    ADD CONSTRAINT fk3195dqlpc076xd2lra7dnx6gr FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: seance fk4e8g1b9ybe9mjno91iw09o0jp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seance
    ADD CONSTRAINT fk4e8g1b9ybe9mjno91iw09o0jp FOREIGN KEY (film_id) REFERENCES public.films(id);


--
-- Name: review fk6cpw2nlklblpvc7hyt7ko6v3e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fk6cpw2nlklblpvc7hyt7ko6v3e FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: billets fkakd1vqcg9sb90iqao46yqd4ix; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billets
    ADD CONSTRAINT fkakd1vqcg9sb90iqao46yqd4ix FOREIGN KEY (seance_id) REFERENCES public.seance(id);


--
-- Name: review fkb6p4gh559hwukapa9mqx0isqq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkb6p4gh559hwukapa9mqx0isqq FOREIGN KEY (film_id) REFERENCES public.films(id);


--
-- Name: user_news fkb8go94ohx3ucvudssnjpfj5b9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_news
    ADD CONSTRAINT fkb8go94ohx3ucvudssnjpfj5b9 FOREIGN KEY (news_id) REFERENCES public.news(id);


--
-- Name: seance fkhupu0bkkkinurpu14xeyjcuep; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seance
    ADD CONSTRAINT fkhupu0bkkkinurpu14xeyjcuep FOREIGN KEY (salle_id) REFERENCES public.salle(id);


--
-- Name: commande fkivebhwh16qy8eckxt4fq34xyh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande
    ADD CONSTRAINT fkivebhwh16qy8eckxt4fq34xyh FOREIGN KEY (person_id) REFERENCES public.users(id);


--
-- Name: billets fkmt1y2r7bq4rflfc0iw4vi1v2o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billets
    ADD CONSTRAINT fkmt1y2r7bq4rflfc0iw4vi1v2o FOREIGN KEY (commande_id) REFERENCES public.commande(id);


--
-- Name: seance fkovptfi7w0dfxfveh7cx9f27mx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seance
    ADD CONSTRAINT fkovptfi7w0dfxfveh7cx9f27mx FOREIGN KEY (tarif_id) REFERENCES public.tarif(id);


--
-- Name: play play___fk_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play
    ADD CONSTRAINT play___fk_person FOREIGN KEY (person_id) REFERENCES public.persons(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: play play_films_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play
    ADD CONSTRAINT play_films_id_fk FOREIGN KEY (film_id) REFERENCES public.films(id);


--
-- PostgreSQL database dump complete
--

