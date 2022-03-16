--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    author_id bigint NOT NULL,
    author_name character varying(30) NOT NULL,
    author_introduction text
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: TABLE author; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.author IS '书籍的作者信息表';


--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    isbn bigint NOT NULL,
    book_name text NOT NULL,
    introduction text,
    book_score smallint DEFAULT 0
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: COLUMN book.book_score; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book.book_score IS '该书籍的综合评分';


--
-- Name: book_chapter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_chapter (
    "ISBN" bigint NOT NULL,
    chapter_id integer NOT NULL,
    chapter_kind smallint NOT NULL,
    chapter_name character varying(30) NOT NULL
);


ALTER TABLE public.book_chapter OWNER TO postgres;

--
-- Name: TABLE book_chapter; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book_chapter IS '书籍的章节目录信息
“ISBN”：书籍的ISBN号
“chapter_id”：书籍的章节Id
“chapter_kind”：书籍章节的类型
      1：正标题    2：副标题    3：子标题
“chapter_name”：章节名称';


--
-- Name: book_holding_kind; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_holding_kind (
    "ISBN" bigint NOT NULL,
    book_kind_id smallint NOT NULL
);


ALTER TABLE public.book_holding_kind OWNER TO postgres;

--
-- Name: TABLE book_holding_kind; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book_holding_kind IS '书籍拥有的类别信息表';


--
-- Name: book_image; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_image (
    book_image_id bigint NOT NULL,
    "ISBN" bigint NOT NULL,
    image_url text NOT NULL
);


ALTER TABLE public.book_image OWNER TO postgres;

--
-- Name: TABLE book_image; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book_image IS '书籍图像的信息表';


--
-- Name: book_kind; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_kind (
    book_kind_id smallint NOT NULL,
    book_kind_name character varying(20) NOT NULL
);


ALTER TABLE public.book_kind OWNER TO postgres;

--
-- Name: TABLE book_kind; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book_kind IS '书籍的类别信息表；通过某种算法， 可以得到书籍的所属类别， 得到的结果应当是存在这个表内的，也就是说， 这里是书籍类别的来源';


--
-- Name: book_review_content; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_review_content (
    book_review_id bigint NOT NULL,
    content_id smallint NOT NULL,
    content_type smallint NOT NULL,
    review_content text
);


ALTER TABLE public.book_review_content OWNER TO postgres;

--
-- Name: TABLE book_review_content; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.book_review_content IS '用户编写的书评的内容;
content_type:   0: 图片信息    1：文本信息';


--
-- Name: book_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_store (
    store_id bigint NOT NULL,
    store_name character varying(60) NOT NULL,
    platform character varying(10) NOT NULL
);


ALTER TABLE public.book_store OWNER TO postgres;

--
-- Name: created_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.created_book (
    "ISBN" bigint NOT NULL,
    author_id bigint NOT NULL
);


ALTER TABLE public.created_book OWNER TO postgres;

--
-- Name: TABLE created_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.created_book IS '已经存在的作者创建的书籍信息表';


--
-- Name: published_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.published_book (
    "ISBN" bigint NOT NULL,
    publisher_id bigint NOT NULL,
    published_date time with time zone NOT NULL
);


ALTER TABLE public.published_book OWNER TO postgres;

--
-- Name: TABLE published_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.published_book IS '已经出版的书籍信息表';


--
-- Name: publisher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.publisher (
    publisher_id bigint NOT NULL,
    publisher_name character varying(30) NOT NULL
);


ALTER TABLE public.publisher OWNER TO postgres;

--
-- Name: TABLE publisher; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.publisher IS '"出版社"表';


--
-- Name: store_selling; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.store_selling (
    store_id bigint NOT NULL,
    "ISBN" bigint NOT NULL,
    book_price double precision NOT NULL,
    buying_url text NOT NULL
);


ALTER TABLE public.store_selling OWNER TO postgres;

--
-- Name: user_book_mark; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_book_mark (
    user_id bigint NOT NULL,
    "ISBN" bigint NOT NULL,
    score smallint NOT NULL,
    comment text,
    mark_date time with time zone NOT NULL
);


ALTER TABLE public.user_book_mark OWNER TO postgres;

--
-- Name: TABLE user_book_mark; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_book_mark IS '用户对于书籍打分的信息表';


--
-- Name: user_book_review; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_book_review (
    book_review_id bigint NOT NULL,
    "ISBN" bigint NOT NULL,
    user_id bigint NOT NULL,
    write_date time with time zone NOT NULL,
    star_num integer DEFAULT 0,
    comment_num integer DEFAULT 0
);


ALTER TABLE public.user_book_review OWNER TO postgres;

--
-- Name: TABLE user_book_review; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_book_review IS '用户地书评信息表';


--
-- Name: COLUMN user_book_review.star_num; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_book_review.star_num IS '该书评的点赞量';


--
-- Name: COLUMN user_book_review.comment_num; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_book_review.comment_num IS '该书评的评论数';


--
-- Name: user_book_review_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_book_review_comment (
    user_id bigint NOT NULL,
    book_review_id bigint NOT NULL,
    comment_content text,
    comment_date time with time zone NOT NULL
);


ALTER TABLE public.user_book_review_comment OWNER TO postgres;

--
-- Name: TABLE user_book_review_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_book_review_comment IS '用户对于书评的评论信息表';


--
-- Name: user_book_review_star; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_book_review_star (
    user_id bigint NOT NULL,
    book_review_id bigint NOT NULL,
    star_date time with time zone NOT NULL,
    star_flag boolean DEFAULT false
);


ALTER TABLE public.user_book_review_star OWNER TO postgres;

--
-- Name: TABLE user_book_review_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_book_review_star IS '用户对于书评的点赞信息记录表。
主要用于记录用户的点赞信息';


--
-- Name: COLUMN user_book_review_star.star_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_book_review_star.star_flag IS '记录当前的用户的点赞状态';


--
-- Name: user_feature; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_feature (
    feature_id smallint NOT NULL,
    feature_name character varying(10) NOT NULL
);


ALTER TABLE public.user_feature OWNER TO postgres;

--
-- Name: TABLE user_feature; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_feature IS '用户的种类信息， 使用某种算法对用户进行分类时， 用户分类的结果将是存在于这个表中的。';


--
-- Name: user_feature_holding; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_feature_holding (
    user_id bigint NOT NULL,
    feature_id bigint NOT NULL
);


ALTER TABLE public.user_feature_holding OWNER TO postgres;

--
-- Name: TABLE user_feature_holding; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_feature_holding IS '用户所属的类型';


--
-- Name: user_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_info (
    user_id bigint NOT NULL,
    user_phone character varying(16),
    user_email character varying(256),
    user_name character varying(30) NOT NULL,
    user_sex character varying(4) NOT NULL,
    head_image text,
    user_password character(128) NOT NULL
);


ALTER TABLE public.user_info OWNER TO postgres;

--
-- Name: TABLE user_info; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_info IS '用户信息表';


--
-- Name: user_share; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_share (
    share_id bigint NOT NULL,
    share_address text NOT NULL,
    star_num integer DEFAULT 0 NOT NULL,
    comment_num integer DEFAULT 0 NOT NULL,
    share_date time with time zone NOT NULL
);


ALTER TABLE public.user_share OWNER TO postgres;

--
-- Name: TABLE user_share; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_share IS '用户分享的读书体会心得信息表；
此表作为一个大概的读书体会心得信息列表， 提供对应的分享内容的地址；
分享的内容将存放在 user_share_content 表中';


--
-- Name: user_share_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_share_comment (
    user_id bigint NOT NULL,
    share_id bigint NOT NULL,
    comment_id integer NOT NULL,
    comment_content text,
    comment_date time with time zone NOT NULL
);


ALTER TABLE public.user_share_comment OWNER TO postgres;

--
-- Name: TABLE user_share_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_share_comment IS '用户对于其他用户分享的读书心得的评论信息记录表';


--
-- Name: user_share_content; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_share_content (
);


ALTER TABLE public.user_share_content OWNER TO postgres;

--
-- Name: TABLE user_share_content; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_share_content IS '用户分享的读书心得的内容；
Content_type: 
    0: 图片类型内容
    1: 正文标题
    2：正文类型
    3：图片注释';


--
-- Name: user_share_star; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_share_star (
    user_id bigint NOT NULL,
    share_id bigint NOT NULL,
    star_flag boolean DEFAULT false,
    star_date time with time zone NOT NULL
);


ALTER TABLE public.user_share_star OWNER TO postgres;

--
-- Name: TABLE user_share_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_share_star IS '用户对于其他用户分享的读书心得的点赞';


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_id);


--
-- Name: book_chapter book_chapter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_chapter
    ADD CONSTRAINT book_chapter_pkey PRIMARY KEY ("ISBN", chapter_id);


--
-- Name: book_holding_kind book_holding_kind_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_holding_kind
    ADD CONSTRAINT book_holding_kind_pkey PRIMARY KEY ("ISBN", book_kind_id);


--
-- Name: book_image book_image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_image
    ADD CONSTRAINT book_image_pkey PRIMARY KEY (book_image_id);


--
-- Name: book_kind book_kind_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_kind
    ADD CONSTRAINT book_kind_pkey PRIMARY KEY (book_kind_id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (isbn);


--
-- Name: book_review_content book_review_content_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_review_content
    ADD CONSTRAINT book_review_content_pkey PRIMARY KEY (book_review_id, content_id);


--
-- Name: book_store book_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_store
    ADD CONSTRAINT book_store_pkey PRIMARY KEY (store_id);


--
-- Name: created_book created_book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.created_book
    ADD CONSTRAINT created_book_pkey PRIMARY KEY ("ISBN", author_id);


--
-- Name: published_book published_book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.published_book
    ADD CONSTRAINT published_book_pkey PRIMARY KEY ("ISBN", publisher_id);


--
-- Name: publisher publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (publisher_id);


--
-- Name: store_selling store_selling_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_selling
    ADD CONSTRAINT store_selling_pkey PRIMARY KEY (store_id);


--
-- Name: user_book_mark user_book_mark_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_mark
    ADD CONSTRAINT user_book_mark_pkey PRIMARY KEY (user_id, "ISBN");


--
-- Name: user_book_review_comment user_book_review_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_comment
    ADD CONSTRAINT user_book_review_comment_pkey PRIMARY KEY (user_id, book_review_id);


--
-- Name: user_book_review user_book_review_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review
    ADD CONSTRAINT user_book_review_pkey PRIMARY KEY (book_review_id);


--
-- Name: user_book_review_star user_book_review_star_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_star
    ADD CONSTRAINT user_book_review_star_pkey PRIMARY KEY (user_id, book_review_id);


--
-- Name: user_feature_holding user_feature_holding_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_holding
    ADD CONSTRAINT user_feature_holding_pkey PRIMARY KEY (user_id, feature_id);


--
-- Name: user_feature user_feature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature
    ADD CONSTRAINT user_feature_pkey PRIMARY KEY (feature_id);


--
-- Name: user_info user_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_info
    ADD CONSTRAINT user_info_pkey PRIMARY KEY (user_id);


--
-- Name: user_share_comment user_share_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_comment
    ADD CONSTRAINT user_share_comment_pkey PRIMARY KEY (user_id, share_id, comment_id);


--
-- Name: user_share user_share_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share
    ADD CONSTRAINT user_share_pkey PRIMARY KEY (share_id);


--
-- Name: user_share_star user_share_star_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_star
    ADD CONSTRAINT user_share_star_pkey PRIMARY KEY (user_id, share_id);


--
-- Name: fki_book_chapter_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_book_chapter_ISBN_foreign_key" ON public.book_chapter USING btree ("ISBN");


--
-- Name: fki_book_holding_kind_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_book_holding_kind_ISBN_foreign_key" ON public.book_holding_kind USING btree ("ISBN");


--
-- Name: fki_book_holding_kind_kind_id_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_book_holding_kind_kind_id_foreign_key ON public.book_holding_kind USING btree (book_kind_id);


--
-- Name: fki_created_book_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_created_book_ISBN_foreign_key" ON public.created_book USING btree ("ISBN");


--
-- Name: fki_created_book_author_id_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_created_book_author_id_foreign_key ON public.created_book USING btree (author_id);


--
-- Name: fki_published_book_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_published_book_ISBN_foreign_key" ON public.published_book USING btree ("ISBN");


--
-- Name: fki_published_book_publisher_id_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_published_book_publisher_id_foreign_key ON public.published_book USING btree (publisher_id);


--
-- Name: fki_s; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_s ON public.store_selling USING btree ("ISBN");


--
-- Name: fki_user_book_mark_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_user_book_mark_ISBN_foreign_key" ON public.user_book_mark USING btree ("ISBN");


--
-- Name: fki_user_book_mark_user_id_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_user_book_mark_user_id_foreign_key ON public.user_book_mark USING btree (user_id);


--
-- Name: fki_user_book_review_ISBN_foreign_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "fki_user_book_review_ISBN_foreign_key" ON public.user_book_review USING btree ("ISBN");


--
-- Name: fki_user_book_review_book_review_id_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_user_book_review_book_review_id_fky ON public.user_book_review_comment USING btree (book_review_id);


--
-- Name: fki_user_book_review_user_id_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_user_book_review_user_id_fky ON public.user_book_review USING btree (user_id);


--
-- Name: book_chapter book_chapter_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_chapter
    ADD CONSTRAINT "book_chapter_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "book_chapter_ISBN_foreign_key" ON book_chapter; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "book_chapter_ISBN_foreign_key" ON public.book_chapter IS '书籍的章节的ISBN必定是来源已经存在的书籍的ISBN';


--
-- Name: book_holding_kind book_holding_kind_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_holding_kind
    ADD CONSTRAINT "book_holding_kind_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "book_holding_kind_ISBN_foreign_key" ON book_holding_kind; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "book_holding_kind_ISBN_foreign_key" ON public.book_holding_kind IS '书籍拥有的类别的ISBN号， 用于指定特定的书籍。';


--
-- Name: book_holding_kind book_holding_kind_kind_id_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_holding_kind
    ADD CONSTRAINT book_holding_kind_kind_id_foreign_key FOREIGN KEY (book_kind_id) REFERENCES public.book_kind(book_kind_id) NOT VALID;


--
-- Name: CONSTRAINT book_holding_kind_kind_id_foreign_key ON book_holding_kind; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT book_holding_kind_kind_id_foreign_key ON public.book_holding_kind IS '书籍拥有的种类的种类ID， 它应当存在于book_kind表中';


--
-- Name: created_book created_book_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.created_book
    ADD CONSTRAINT "created_book_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "created_book_ISBN_foreign_key" ON created_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "created_book_ISBN_foreign_key" ON public.created_book IS '已经创建的书籍的ISBN一定来源于已经存在的书籍';


--
-- Name: created_book created_book_author_id_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.created_book
    ADD CONSTRAINT created_book_author_id_foreign_key FOREIGN KEY (author_id) REFERENCES public.author(author_id) NOT VALID;


--
-- Name: CONSTRAINT created_book_author_id_foreign_key ON created_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT created_book_author_id_foreign_key ON public.created_book IS '已经创建的书籍的作者一定来源已经存在的作者';


--
-- Name: published_book published_book_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.published_book
    ADD CONSTRAINT "published_book_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "published_book_ISBN_foreign_key" ON published_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "published_book_ISBN_foreign_key" ON public.published_book IS '出版的书籍必定是来源于已经存在的书籍';


--
-- Name: published_book published_book_publisher_id_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.published_book
    ADD CONSTRAINT published_book_publisher_id_foreign_key FOREIGN KEY (publisher_id) REFERENCES public.publisher(publisher_id) NOT VALID;


--
-- Name: CONSTRAINT published_book_publisher_id_foreign_key ON published_book; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT published_book_publisher_id_foreign_key ON public.published_book IS '出版的书籍必定有一个已经存在的出版社';


--
-- Name: store_selling store_selling_book_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_selling
    ADD CONSTRAINT "store_selling_book_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "store_selling_book_ISBN_foreign_key" ON store_selling; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "store_selling_book_ISBN_foreign_key" ON public.store_selling IS '"store_selling" 的"ISBN"列来源于 "book" 的"ISBN"， 确保所卖的书籍确实存在';


--
-- Name: store_selling store_selling_store_id_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_selling
    ADD CONSTRAINT store_selling_store_id_foreign_key FOREIGN KEY (store_id) REFERENCES public.book_store(store_id) NOT VALID;


--
-- Name: CONSTRAINT store_selling_store_id_foreign_key ON store_selling; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT store_selling_store_id_foreign_key ON public.store_selling IS '“store_selling”  的 “store_id” 源自 “book_store”表中的 "store_id"。用于保证所卖的书籍有一个明确的店铺';


--
-- Name: user_book_mark user_book_mark_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_mark
    ADD CONSTRAINT "user_book_mark_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "user_book_mark_ISBN_foreign_key" ON user_book_mark; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "user_book_mark_ISBN_foreign_key" ON public.user_book_mark IS '用户打分的书籍的ISBN， 用户打分的书籍必定是已经存在的';


--
-- Name: user_book_mark user_book_mark_user_id_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_mark
    ADD CONSTRAINT user_book_mark_user_id_foreign_key FOREIGN KEY (user_id) REFERENCES public.user_info(user_id) NOT VALID;


--
-- Name: CONSTRAINT user_book_mark_user_id_foreign_key ON user_book_mark; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_mark_user_id_foreign_key ON public.user_book_mark IS '用户评分表的用户ID关联外键名；
用户评分一定是用户参与的';


--
-- Name: user_book_review user_book_review_ISBN_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review
    ADD CONSTRAINT "user_book_review_ISBN_foreign_key" FOREIGN KEY ("ISBN") REFERENCES public.book(isbn) NOT VALID;


--
-- Name: CONSTRAINT "user_book_review_ISBN_foreign_key" ON user_book_review; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT "user_book_review_ISBN_foreign_key" ON public.user_book_review IS '用户编写书评时指定的书籍应当已经存在于book表中';


--
-- Name: user_book_review_star user_book_review_book_review_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_star
    ADD CONSTRAINT user_book_review_book_review_id_fky FOREIGN KEY (book_review_id) REFERENCES public.user_book_review(book_review_id);


--
-- Name: CONSTRAINT user_book_review_book_review_id_fky ON user_book_review_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_review_book_review_id_fky ON public.user_book_review_star IS '用户点赞时书评一定是已经存在的';


--
-- Name: user_book_review_comment user_book_review_book_review_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_comment
    ADD CONSTRAINT user_book_review_book_review_id_fky FOREIGN KEY (book_review_id) REFERENCES public.user_book_review(book_review_id) NOT VALID;


--
-- Name: CONSTRAINT user_book_review_book_review_id_fky ON user_book_review_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_review_book_review_id_fky ON public.user_book_review_comment IS '用户评论书评时该书评一定是已经存在的';


--
-- Name: user_book_review_comment user_book_review_comment_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_comment
    ADD CONSTRAINT user_book_review_comment_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id);


--
-- Name: CONSTRAINT user_book_review_comment_user_id_fky ON user_book_review_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_review_comment_user_id_fky ON public.user_book_review_comment IS '用户评论书评是用户一定是已经存在的';


--
-- Name: user_book_review user_book_review_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review
    ADD CONSTRAINT user_book_review_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id) NOT VALID;


--
-- Name: CONSTRAINT user_book_review_user_id_fky ON user_book_review; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_review_user_id_fky ON public.user_book_review IS '用户编写的书评时用户的信息是已经存在于user_info表内的';


--
-- Name: user_book_review_star user_book_review_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_book_review_star
    ADD CONSTRAINT user_book_review_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id);


--
-- Name: CONSTRAINT user_book_review_user_id_fky ON user_book_review_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_book_review_user_id_fky ON public.user_book_review_star IS '用户点赞时用户一定是已经存在的';


--
-- Name: user_feature_holding user_feature_feature_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_holding
    ADD CONSTRAINT user_feature_feature_id_fky FOREIGN KEY (feature_id) REFERENCES public.user_feature(feature_id);


--
-- Name: CONSTRAINT user_feature_feature_id_fky ON user_feature_holding; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_feature_feature_id_fky ON public.user_feature_holding IS '用户所属类型的类型ID， 应当已经存在';


--
-- Name: user_feature_holding user_feature_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_feature_holding
    ADD CONSTRAINT user_feature_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id);


--
-- Name: CONSTRAINT user_feature_user_id_fky ON user_feature_holding; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_feature_user_id_fky ON public.user_feature_holding IS '用户所属类型的用户ID， 用户应当是已经存在的';


--
-- Name: user_share_comment user_share_comment_share_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_comment
    ADD CONSTRAINT user_share_comment_share_id_fky FOREIGN KEY (share_id) REFERENCES public.user_share(share_id);


--
-- Name: CONSTRAINT user_share_comment_share_id_fky ON user_share_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_share_comment_share_id_fky ON public.user_share_comment IS '用户对于其他用户分享的读书心得进行评论时， 该读书心得应当是已经存在的。';


--
-- Name: user_share_comment user_share_comment_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_comment
    ADD CONSTRAINT user_share_comment_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id);


--
-- Name: CONSTRAINT user_share_comment_user_id_fky ON user_share_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_share_comment_user_id_fky ON public.user_share_comment IS '用户对于其他用户分享的读书心得进行评论是， 当前的用户应当是已经存在的';


--
-- Name: user_share_star user_share_star_share_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_star
    ADD CONSTRAINT user_share_star_share_id_fky FOREIGN KEY (share_id) REFERENCES public.user_share(share_id);


--
-- Name: CONSTRAINT user_share_star_share_id_fky ON user_share_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_share_star_share_id_fky ON public.user_share_star IS '用户对于其他用户分享的信息点赞时， 分享的读书心得此时应当已经存在';


--
-- Name: user_share_star user_share_star_user_id_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_share_star
    ADD CONSTRAINT user_share_star_user_id_fky FOREIGN KEY (user_id) REFERENCES public.user_info(user_id);


--
-- Name: CONSTRAINT user_share_star_user_id_fky ON user_share_star; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT user_share_star_user_id_fky ON public.user_share_star IS '用户对于其他用户的读书心得的点赞， 此时用户应当已经存在';


--
-- PostgreSQL database dump complete
--

