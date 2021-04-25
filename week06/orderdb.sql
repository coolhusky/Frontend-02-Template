CREATE DATABASE IF NOT EXISTS orderdb;
USE orderdb;
-- user
CREATE TABLE IF NOT EXISTS t_user
(
    id               BIGINT AUTO_INCREMENT,
    username         VARCHAR(32) NOT NULL DEFAULT '',
    password         VARCHAR(32) NOT NULL DEFaulT '',
    nickname         VARCHAR(32) NOT NULL DEFAULT '',
    name             VARCHAR(32) NOT NULL DEFAULT '',
    tel_country_code VARCHAR(8)  NOT NULL DEFAULT '',
    tel_number       VARCHAR(8)  NOT NULL DEFAULT '',
    create_time      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted          BIT(1)      NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
-- spu, t_product, t_product_category, t_product_attr
CREATE TABLE IF NOT EXISTS t_product
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL DEFAULT '',
    category_id BIGINT       NOT NULL DEFAULT 0,
    create_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)       NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
CREATE TABLE IF NOT EXISTS t_product_category
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(128)     NOT NULL DEFAULT '',
    parent_id   BIGINT           NOT NULL DEFAULT 0,
    level       TINYINT UNSIGNED NOT NULL DEFAULT 0,
    create_time DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)           NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
CREATE TABLE IF NOT EXISTS t_product_attr
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL DEFAULT '',
    product_id  BIGINT       NOT NULL DEFAULT 0,
    category_id BIGINT       NOT NULL DEFAULT 0,
    create_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)       NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
CREATE TABLE IF NOT EXISTS t_product_attr_value
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL DEFAULT '',
    attr_id     BIGINT       NOT NULL DEFAULT 0,
    create_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)       NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

-- sku
CREATE TABLE IF NOT EXISTS t_product_sku
(
    id          BIGINT AUTO_INCREMENT,
    product_id  BIGINT         NOT NULL DEFAULT 0,
    price       DECIMAL(12, 3) NOT NULL DEFAULT 0.000,
    stock       INT            NOT NULL DEFAULT 0,
    create_time DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)         NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
CREATE TABLE IF NOT EXISTS t_product_sku
(
    id                  BIGINT AUTO_INCREMENT,
    product_id          BIGINT         NOT NULL DEFAULT 0,
    price               DECIMAL(12, 3) NOT NULL DEFAULT 0.000,
    stock               INT            NOT NULL DEFAULT 0,
    image_url           VARCHAR(256)   NOT NULL DEFAULT '',
    image_thumbnail_url VARCHAR(256)   NOT NULL DEFAULT '',
    create_time         DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time         DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted             BIT(1)         NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
CREATE TABLE IF NOT EXISTS t_product_sku_attr
(
    id          BIGINT AUTO_INCREMENT,
    sku_id      BIGINT      NOT NULL DEFAULT 0,
    attr_id     BIGINT      NOT NULL DEFAULT 0,
    value_id    BIGINT      NOT NULL DEFAULT 0,
    create_time DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted     BIT(1)      NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

-- order
CREATE TABLE IF NOT EXISTS t_order
(
    id                        BIGINT AUTO_INCREMENT,
    status                    INT          NOT NULL DEFAULT 0,
    sku_id                    BIGINT       NOT NULL DEFAULT 0,
    user_id                   BIGINT       NOT NULL DEFAULT 0,
    vendor_id                 BIGINT       NOT NULL DEFAULT 0,
    vendor_name               VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_name             VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_tel_country_code VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_tel_number       VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_country          VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_province         VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_city             VARCHAR(64)  NOT NULL DEFAULT '',
    receiver_address          VARCHAR(256) NOT NULL DEFAULT '',
    create_time               DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time               DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted                   BIT(1)       NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
