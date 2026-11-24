-- 基础设置
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 用户表及关联表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `role` tinyint NOT NULL COMMENT '角色：1-管理员，2-销售人员，3-客户',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 COMMENT='用户表';

INSERT INTO `user` VALUES
    (1,'1','$2a$10$IiVT3D.bJ4GcgJfIZfF2KOSvNHdqRFOLFZbHtZFZO1FRKAyqXjacO','1','1','11.qq.com',1,'2025-06-13 18:24:20','2025-06-14 01:04:29'),
    (3,'3','$2a$10$IiVT3D.bJ4GcgJfIZfF2KOSvNHdqRFOLFZbHtZFZO1FRKAyqXjacO','111','111','111@qq.com',3,'2025-06-13 23:47:20','2025-06-14 01:04:24'),
    (5,'qwe','$2a$10$KG/Eh19Z6M188tSJVKk2xO0MuuftFCWj4HYaU5yUkE6LqT/CtaJj2','qwe','qwe','qwe1@qq.com',2,'2025-06-14 00:16:06','2025-06-14 00:16:06');

-- 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `admin_id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `position` varchar(50) DEFAULT NULL COMMENT '职位',
    `department` varchar(50) DEFAULT NULL COMMENT '部门',
    PRIMARY KEY (`admin_id`),
    UNIQUE KEY `user_id` (`user_id`),
    CONSTRAINT `fk_admin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT='管理员表';

INSERT INTO `admin` VALUES (1,1,'1','');

-- 客户表
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `customer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `company` varchar(100) DEFAULT NULL COMMENT '公司名称',
    `industry` varchar(50) DEFAULT NULL COMMENT '所属行业',
    `address` varchar(255) DEFAULT NULL COMMENT '地址',
    `city` varchar(50) DEFAULT NULL COMMENT '城市',
    `province` varchar(50) DEFAULT NULL COMMENT '省份',
    `postal_code` varchar(20) DEFAULT NULL COMMENT '邮政编码',
    PRIMARY KEY (`customer_id`),
    UNIQUE KEY `user_id` (`user_id`),
    CONSTRAINT `fk_customer_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 COMMENT='客户表';

INSERT INTO `customer` VALUES
    (1,2,'1111','1','11','sz','guangdong','111'),
    (2,3,'111','11','1','gz','guangdong','11');

-- 销售人员表
DROP TABLE IF EXISTS `salesperson`;
CREATE TABLE `salesperson` (
     `salesperson_id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售人员ID',
     `user_id` bigint NOT NULL COMMENT '用户ID',
     `employee_id` varchar(50) DEFAULT NULL COMMENT '员工编号',
     `department` varchar(50) DEFAULT NULL COMMENT '部门',
     `position` varchar(50) DEFAULT NULL COMMENT '职位',
     `hire_date` date DEFAULT NULL COMMENT '入职日期',
     PRIMARY KEY (`salesperson_id`),
     UNIQUE KEY `user_id` (`user_id`),
     UNIQUE KEY `employee_id` (`employee_id`),
     CONSTRAINT `fk_salesperson_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT='销售人员表';

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
     `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
     `order_no` varchar(50) NOT NULL COMMENT '订单编号',
     `customer_id` bigint NOT NULL COMMENT '客户ID',
     `salesperson_id` bigint NOT NULL COMMENT '销售人员ID',
     `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     PRIMARY KEY (`order_id`),
     UNIQUE KEY `order_no` (`order_no`),
     KEY `fk_order_customer` (`customer_id`),
     KEY `fk_order_salesperson` (`salesperson_id`),
     CONSTRAINT `fk_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
     CONSTRAINT `fk_order_salesperson` FOREIGN KEY (`salesperson_id`) REFERENCES `salesperson` (`salesperson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT='订单表';

INSERT INTO `order` VALUES (1,'1',2,1,1.00,'2025-06-14 01:38:14');

-- 订单明细表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
     `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
     `order_id` bigint NOT NULL COMMENT '订单ID',
     `product_id` bigint NOT NULL COMMENT '产品ID',
     `quantity` int NOT NULL COMMENT '数量',
     `price` decimal(10,2) NOT NULL COMMENT '单价',
     `subtotal` decimal(10,2) NOT NULL COMMENT '小计',
     `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
     `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`item_id`),
     KEY `fk_orderitem_order` (`order_id`),
     KEY `fk_orderitem_product` (`product_id`),
     CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
     CONSTRAINT `fk_orderitem_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB COMMENT='订单明细表';

-- 产品类别表
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
     `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '类别ID',
     `category_name` varchar(50) NOT NULL COMMENT '类别名称',
     `description` varchar(255) DEFAULT NULL COMMENT '类别描述',
     `parent_id` bigint DEFAULT NULL COMMENT '父类别ID',
     PRIMARY KEY (`category_id`),
     KEY `fk_category_parent` (`parent_id`),
     CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `product_category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT='产品类别表';

INSERT INTO `product_category` VALUES (1,'1231','2313',NULL,1);

-- 产品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
    `product_name` varchar(100) NOT NULL COMMENT '产品名称',
    `product_code` varchar(50) DEFAULT NULL COMMENT '产品编码',
    `category_id` bigint DEFAULT NULL COMMENT '类别ID',
    `price` decimal(10,2) NOT NULL COMMENT '产品价格',
    `description` text COMMENT '产品描述',
    `stock` int DEFAULT 0 COMMENT '库存数量',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`product_id`),
    UNIQUE KEY `product_code` (`product_code`),
    KEY `fk_product_category` (`category_id`),
    CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `product_category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT='产品表';

INSERT INTO `product` VALUES (1,'4444',NULL,NULL,44.00,'11',11,'2025-06-14 00:27:08');

-- 产品收藏表
DROP TABLE IF EXISTS `product_favorite`;
CREATE TABLE `product_favorite` (
    `favorite_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `customer_id` bigint NOT NULL COMMENT '客户ID',
    `product_id` bigint NOT NULL COMMENT '产品ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`favorite_id`),
    UNIQUE KEY `uk_customer_product` (`customer_id`,`product_id`),
    KEY `fk_favorite_product` (`product_id`),
    CONSTRAINT `fk_favorite_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    CONSTRAINT `fk_favorite_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB COMMENT='产品收藏表';

-- 销售记录表
DROP TABLE IF EXISTS `sales_record`;
CREATE TABLE `sales_record` (
    `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `salesperson_id` bigint NOT NULL COMMENT '销售人员ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `record_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    PRIMARY KEY (`record_id`),
    KEY `fk_salesrecord_salesperson` (`salesperson_id`),
    KEY `fk_salesrecord_order` (`order_id`),
    CONSTRAINT `fk_salesrecord_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
    CONSTRAINT `fk_salesrecord_salesperson` FOREIGN KEY (`salesperson_id`) REFERENCES `salesperson` (`salesperson_id`)
) ENGINE=InnoDB COMMENT='销售记录表';

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;