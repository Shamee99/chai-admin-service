/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 170005 (170005)
 Source Host           : localhost:5432
 Source Catalog        : chai-admin
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170005 (170005)
 File Encoding         : 65001

 Date: 19/07/2025 17:14:38
*/


-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
                                     "id" int8 NOT NULL DEFAULT nextval('sys_dept_id_seq'::regclass),
                                     "parent_id" int8 DEFAULT 0,
                                     "dept_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "dept_code" varchar(50) COLLATE "pg_catalog"."default",
                                     "leader" varchar(50) COLLATE "pg_catalog"."default",
                                     "phone" varchar(20) COLLATE "pg_catalog"."default",
                                     "email" varchar(100) COLLATE "pg_catalog"."default",
                                     "sort_order" int4 DEFAULT 0,
                                     "status" int4 DEFAULT 1,
                                     "remark" varchar(500) COLLATE "pg_catalog"."default",
                                     "create_by" int8,
                                     "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8,
                                     "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "deleted" bool DEFAULT false,
                                     "version" int4 DEFAULT 1
)
;
COMMENT ON COLUMN "public"."sys_dept"."parent_id" IS '父部门ID';
COMMENT ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_dept"."dept_code" IS '部门编码';
COMMENT ON COLUMN "public"."sys_dept"."leader" IS '负责人';
COMMENT ON COLUMN "public"."sys_dept"."phone" IS '联系电话';
COMMENT ON COLUMN "public"."sys_dept"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_dept"."sort_order" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_dept"."status" IS '部门状态：0-禁用，1-启用';
COMMENT ON COLUMN "public"."sys_dept"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dept"."deleted" IS '删除标志：0-未删除，1-已删除';
COMMENT ON TABLE "public"."sys_dept" IS '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (1, 0, 'Chai科技', 'CHAI_TECH', 'admin', NULL, NULL, 0, 1, '总公司', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_dept" VALUES (2, 1, '研发部门', 'DEV_DEPT', 'dev_leader', NULL, NULL, 1, 1, '研发部门', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_dept" VALUES (3, 1, '市场部门', 'MARKET_DEPT', 'market_leader', NULL, NULL, 2, 1, '市场部门', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_dept" VALUES (4, 1, '测试部门', 'TEST_DEPT', 'test_leader', NULL, NULL, 3, 1, '测试部门', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_dept" VALUES (5, 1, '财务部门', 'FINANCE_DEPT', 'finance_leader', NULL, NULL, 4, 1, '财务部门', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_dept" VALUES (1943885475721175042, 0, '大部门', '12', '', '13333333333', '', 0, 1, NULL, 1, '2025-07-12 12:09:47.717857', 1, '2025-07-19 17:03:06.88099', 't', 1);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_login_log";
CREATE TABLE "public"."sys_login_log" (
                                          "id" int8 NOT NULL DEFAULT nextval('sys_login_log_id_seq'::regclass),
                                          "username" varchar(50) COLLATE "pg_catalog"."default",
                                          "ipaddr" varchar(50) COLLATE "pg_catalog"."default",
                                          "login_location" varchar(255) COLLATE "pg_catalog"."default",
                                          "browser" varchar(50) COLLATE "pg_catalog"."default",
                                          "os" varchar(50) COLLATE "pg_catalog"."default",
                                          "status" int4 DEFAULT 0,
                                          "msg" text COLLATE "pg_catalog"."default",
                                          "login_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sys_login_log"."username" IS '用户账号';
COMMENT ON COLUMN "public"."sys_login_log"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "public"."sys_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."sys_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."sys_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."sys_login_log"."status" IS '登录状态：0-成功，1-失败';
COMMENT ON COLUMN "public"."sys_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."sys_login_log"."login_time" IS '访问时间';
COMMENT ON TABLE "public"."sys_login_log" IS '系统访问记录';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
                                     "id" int8 NOT NULL DEFAULT nextval('sys_menu_id_seq'::regclass),
                                     "parent_id" int8 DEFAULT 0,
                                     "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "menu_type" int4 NOT NULL,
                                     "path" varchar(200) COLLATE "pg_catalog"."default",
                                     "component" varchar(255) COLLATE "pg_catalog"."default",
                                     "permissions" varchar(100) COLLATE "pg_catalog"."default",
                                     "icon" varchar(100) COLLATE "pg_catalog"."default",
                                     "sort_order" int4 DEFAULT 0,
                                     "is_visible" int4 DEFAULT 1,
                                     "status" int4 DEFAULT 1,
                                     "is_external" int4 DEFAULT 0,
                                     "is_keepalive" int4 DEFAULT 0,
                                     "remark" varchar(500) COLLATE "pg_catalog"."default",
                                     "create_by" int8,
                                     "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8,
                                     "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "deleted" bool DEFAULT false,
                                     "version" int4 DEFAULT 1
)
;
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型：1-目录，2-菜单，3-按钮';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件路径';
COMMENT ON COLUMN "public"."sys_menu"."permissions" IS '权限标识';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."sys_menu"."sort_order" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_menu"."is_visible" IS '菜单状态：0-隐藏，1-显示';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '菜单状态：0-禁用，1-启用';
COMMENT ON COLUMN "public"."sys_menu"."is_external" IS '是否为外链：0-否，1-是';
COMMENT ON COLUMN "public"."sys_menu"."is_keepalive" IS '是否缓存：0-不缓存，1-缓存';
COMMENT ON COLUMN "public"."sys_menu"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."deleted" IS '删除标志：0-未删除，1-已删除';
COMMENT ON TABLE "public"."sys_menu" IS '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (403, 5, '部门删除', 3, '', '', NULL, NULL, 4, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (204, 3, '角色导出', 3, '', '', NULL, NULL, 5, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (300, 4, '菜单查询', 3, '', '', NULL, NULL, 1, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (100, 2, '用户查询', 3, '', '', NULL, NULL, 1, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (101, 2, '用户新增', 3, '', '', NULL, NULL, 2, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (102, 2, '用户修改', 3, '', '', NULL, NULL, 3, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (4, 1, '菜单管理', 2, '/system/menu', '/system/menu/MenuView', NULL, 'Memo', 3, 1, 1, 0, 0, '菜单管理菜单', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (104, 2, '用户导出', 3, '', '', NULL, NULL, 5, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (105, 2, '用户导入', 3, '', '', NULL, NULL, 6, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (2, 1, '用户管理', 2, '/system/user', '/system/user/UserView', NULL, 'User', 1, 1, 1, 0, 0, '用户管理菜单', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (5, 1, '部门管理', 2, '/system/dept', '/system/dept/DeptView', NULL, 'OfficeBuilding', 4, 1, 1, 0, 0, '部门管理菜单', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (103, 2, '用户删除', 3, NULL, NULL, NULL, NULL, 4, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (106, 2, '重置密码', 3, '', '', NULL, NULL, 7, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (200, 3, '角色查询', 3, '', '', NULL, NULL, 1, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (201, 3, '角色新增', 3, '', '', NULL, NULL, 2, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (202, 3, '角色修改', 3, '', '', NULL, NULL, 3, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (203, 3, '角色删除', 3, '', '', NULL, NULL, 4, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (301, 4, '菜单新增', 3, '', '', NULL, NULL, 2, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (302, 4, '菜单修改', 3, '', '', NULL, NULL, 3, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (303, 4, '菜单删除', 3, '', '', NULL, NULL, 4, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (400, 5, '部门查询', 3, '', '', NULL, NULL, 1, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (401, 5, '部门新增', 3, '', '', NULL, NULL, 2, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (402, 5, '部门修改', 3, '', '', NULL, NULL, 3, 1, 1, 0, 0, '', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (3, 1, '角色管理', 2, '/system/role', '/system/role/RoleView', NULL, 'Brush', 2, 1, 1, 0, 0, '角色管理菜单', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', NULL);
INSERT INTO "public"."sys_menu" VALUES (1, NULL, '系统管理', 1, '/system', NULL, NULL, 'Monitor', 1, 1, 1, 0, 0, '系统管理目录', 1, '2025-07-03 21:15:01.460808', 1, '2025-07-19 16:56:14.94617', 'f', NULL);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_operation_log";
CREATE TABLE "public"."sys_operation_log" (
                                              "id" int8 NOT NULL DEFAULT nextval('sys_operation_log_id_seq'::regclass),
                                              "title" varchar(50) COLLATE "pg_catalog"."default",
                                              "business_type" int4 DEFAULT 0,
                                              "method" varchar(100) COLLATE "pg_catalog"."default",
                                              "request_method" varchar(10) COLLATE "pg_catalog"."default",
                                              "operator_type" int4 DEFAULT 0,
                                              "oper_name" varchar(50) COLLATE "pg_catalog"."default",
                                              "dept_name" varchar(50) COLLATE "pg_catalog"."default",
                                              "oper_url" varchar(255) COLLATE "pg_catalog"."default",
                                              "oper_ip" varchar(50) COLLATE "pg_catalog"."default",
                                              "oper_location" varchar(255) COLLATE "pg_catalog"."default",
                                              "oper_param" text COLLATE "pg_catalog"."default",
                                              "json_result" text COLLATE "pg_catalog"."default",
                                              "status" int4 DEFAULT 0,
                                              "error_msg" varchar(2000) COLLATE "pg_catalog"."default",
                                              "oper_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                              "cost_time" int8 DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_operation_log"."title" IS '模块标题';
COMMENT ON COLUMN "public"."sys_operation_log"."business_type" IS '业务类型：0-其它，1-新增，2-修改，3-删除，4-授权，5-导出，6-导入，7-强退，8-生成代码，9-清空数据';
COMMENT ON COLUMN "public"."sys_operation_log"."method" IS '方法名称';
COMMENT ON COLUMN "public"."sys_operation_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."sys_operation_log"."operator_type" IS '操作类别：0-其它，1-后台用户，2-手机端用户';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "public"."sys_operation_log"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "public"."sys_operation_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "public"."sys_operation_log"."status" IS '操作状态：0-正常，1-异常';
COMMENT ON COLUMN "public"."sys_operation_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."sys_operation_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "public"."sys_operation_log"."cost_time" IS '消耗时间';
COMMENT ON TABLE "public"."sys_operation_log" IS '操作日志记录';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
                                     "id" int8 NOT NULL DEFAULT nextval('sys_role_id_seq'::regclass),
                                     "role_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "role_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "role_desc" varchar(200) COLLATE "pg_catalog"."default",
                                     "data_scope" int4 DEFAULT 1,
                                     "status" int4 DEFAULT 1,
                                     "sort_order" int4 DEFAULT 0,
                                     "remark" varchar(500) COLLATE "pg_catalog"."default",
                                     "create_by" int8,
                                     "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8,
                                     "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "deleted" bool DEFAULT false,
                                     "version" int4 DEFAULT 1
)
;
COMMENT ON COLUMN "public"."sys_role"."role_code" IS '角色编码';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_desc" IS '角色描述';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据范围：1-全部数据，2-本部门数据，3-本部门及以下数据，4-仅本人数据';
COMMENT ON COLUMN "public"."sys_role"."status" IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN "public"."sys_role"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."deleted" IS '删除标志：0-未删除，1-已删除';
COMMENT ON TABLE "public"."sys_role" IS '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (3, 'USER', '普通用户', '普通用户', 4, 1, 3, '普通用户', 1, '2025-07-03 21:15:01.460808', NULL, '2025-07-03 21:15:01.460808', 'f', 1);
INSERT INTO "public"."sys_role" VALUES (1, 'SUPER_ADMIN', '超级管理员', '超级管理员', 1, 1, 1, '超级管理员', 1, '2025-07-03 21:15:01.460808', 1, '2025-07-16 22:31:27.951686', 'f', 1);
INSERT INTO "public"."sys_role" VALUES (2, 'ADMIN', '管理员', '管理员', 2, 1, 1, '管理员', 1, '2025-07-03 21:15:01.460808', 1, '2025-07-19 16:49:05.528532', 'f', 1);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept" (
                                          "id" int8 NOT NULL DEFAULT nextval('sys_role_dept_id_seq'::regclass),
                                          "role_id" int8 NOT NULL,
                                          "dept_id" int8 NOT NULL,
                                          "create_by" int8,
                                          "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sys_role_dept"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_dept"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_role_dept"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_role_dept"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_role_dept" IS '角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "public"."sys_role_dept" VALUES (1, 1, 4, NULL, '2025-07-12 10:43:09.624696');
INSERT INTO "public"."sys_role_dept" VALUES (2, 2, 4, NULL, '2025-07-12 10:43:18.765173');
INSERT INTO "public"."sys_role_dept" VALUES (3, 3, 5, NULL, '2025-07-12 10:43:27.506504');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu" (
                                          "id" int8 NOT NULL DEFAULT nextval('sys_role_menu_id_seq'::regclass),
                                          "role_id" int8 NOT NULL,
                                          "menu_id" int8 NOT NULL,
                                          "create_by" int8,
                                          "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_role_menu"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_role_menu"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305537, 1, 1, 1, '2025-07-19 17:04:50.715129');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305538, 1, 2, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305539, 1, 100, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305540, 1, 101, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305541, 1, 102, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305542, 1, 103, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305543, 1, 104, 1, '2025-07-19 17:04:50.716126');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305544, 1, 105, 1, '2025-07-19 17:04:50.717133');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305545, 1, 106, 1, '2025-07-19 17:04:50.717133');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305546, 1, 3, 1, '2025-07-19 17:04:50.717133');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305547, 1, 200, 1, '2025-07-19 17:04:50.717133');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305548, 1, 201, 1, '2025-07-19 17:04:50.717133');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305549, 1, 202, 1, '2025-07-19 17:04:50.718127');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305550, 1, 203, 1, '2025-07-19 17:04:50.718127');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305551, 1, 204, 1, '2025-07-19 17:04:50.718127');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305552, 1, 4, 1, '2025-07-19 17:04:50.718631');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305553, 1, 300, 1, '2025-07-19 17:04:50.718631');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305554, 1, 301, 1, '2025-07-19 17:04:50.718631');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305555, 1, 302, 1, '2025-07-19 17:04:50.718631');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305556, 1, 303, 1, '2025-07-19 17:04:50.718631');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305557, 1, 5, 1, '2025-07-19 17:04:50.719636');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305558, 1, 400, 1, '2025-07-19 17:04:50.719636');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442506305559, 1, 401, 1, '2025-07-19 17:04:50.719636');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442569220098, 1, 402, 1, '2025-07-19 17:04:50.720641');
INSERT INTO "public"."sys_role_menu" VALUES (1946496442569220099, 1, 403, 1, '2025-07-19 17:04:50.720641');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014273, 3, 2, 1, '2025-07-19 17:04:56.34712');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014274, 3, 100, 1, '2025-07-19 17:04:56.34712');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014275, 3, 101, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014276, 3, 102, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014277, 3, 103, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014278, 3, 104, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014279, 3, 105, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014280, 3, 106, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496466137014281, 3, 1, 1, '2025-07-19 17:04:56.348626');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480443785218, 2, 1, 1, '2025-07-19 17:04:59.764919');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480443785219, 2, 2, 1, '2025-07-19 17:04:59.764919');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480443785220, 2, 100, 1, '2025-07-19 17:04:59.765924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699777, 2, 101, 1, '2025-07-19 17:04:59.765924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699778, 2, 102, 1, '2025-07-19 17:04:59.765924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699779, 2, 103, 1, '2025-07-19 17:04:59.765924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699780, 2, 104, 1, '2025-07-19 17:04:59.766924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699781, 2, 105, 1, '2025-07-19 17:04:59.766924');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699782, 2, 106, 1, '2025-07-19 17:04:59.767926');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699783, 2, 3, 1, '2025-07-19 17:04:59.767926');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699784, 2, 200, 1, '2025-07-19 17:04:59.767926');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699785, 2, 201, 1, '2025-07-19 17:04:59.767926');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699786, 2, 202, 1, '2025-07-19 17:04:59.767926');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699787, 2, 203, 1, '2025-07-19 17:04:59.769048');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699788, 2, 204, 1, '2025-07-19 17:04:59.769048');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699789, 2, 4, 1, '2025-07-19 17:04:59.769048');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699790, 2, 300, 1, '2025-07-19 17:04:59.769048');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699791, 2, 301, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699792, 2, 302, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699793, 2, 303, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699794, 2, 5, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699795, 2, 400, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699796, 2, 401, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699797, 2, 402, 1, '2025-07-19 17:04:59.77005');
INSERT INTO "public"."sys_role_menu" VALUES (1946496480506699798, 2, 403, 1, '2025-07-19 17:04:59.77005');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
                                     "id" int8 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass),
                                     "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "password" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "real_name" varchar(50) COLLATE "pg_catalog"."default",
                                     "email" varchar(100) COLLATE "pg_catalog"."default",
                                     "phone" varchar(20) COLLATE "pg_catalog"."default",
                                     "avatar" varchar(255) COLLATE "pg_catalog"."default",
                                     "gender" int4 DEFAULT 0,
                                     "birthday" date,
                                     "dept_id" int8,
                                     "status" int4 DEFAULT 1,
                                     "last_login_time" timestamp(6),
                                     "last_login_ip" varchar(50) COLLATE "pg_catalog"."default",
                                     "remark" varchar(500) COLLATE "pg_catalog"."default",
                                     "create_by" int8,
                                     "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8,
                                     "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "deleted" bool DEFAULT false,
                                     "salt" varchar(32) COLLATE "pg_catalog"."default",
                                     "nickname" varchar(100) COLLATE "pg_catalog"."default",
                                     "version" int4 DEFAULT 1
)
;
COMMENT ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."real_name" IS '真实姓名';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像';
COMMENT ON COLUMN "public"."sys_user"."gender" IS '性别：0-未知，1-男，2-女';
COMMENT ON COLUMN "public"."sys_user"."birthday" IS '生日';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_user"."status" IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN "public"."sys_user"."last_login_time" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."last_login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."deleted" IS '删除标志：0-未删除，1-已删除';
COMMENT ON COLUMN "public"."sys_user"."salt" IS '密码盐值';
COMMENT ON TABLE "public"."sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1944015744511930370, 'shamee', '$2a$10$neIeGCqRigaLgBKzbWHOeus90nWbOlRWYYnp1PAPgLhl2p/Lmx/IG', '柴犬哥哥', '11@qq.com', '13313131313', NULL, 1, NULL, 2, 1, '2025-07-12 20:51:23.819539', '0:0:0:0:0:0:0:1', NULL, 1, '2025-07-12 20:47:26.215916', 1, '2025-07-12 20:58:18.807845', 'f', '82LzuEoOJGUIAhTkL5ffYQ==', '柴犬', 1);
INSERT INTO "public"."sys_user" VALUES (6801109150106521601, 'admin', '$2a$10$3T6KzMM6eJZVLFyKJ9VHF.U9UKFyGOf1TwOzzBdxZaJcgObHlTsFm', '演示管理员', '', '', NULL, 0, NULL, 2, 1, NULL, NULL, NULL, 1, '2025-07-19 17:04:18.56153', 1, '2025-07-19 17:04:18.56153', 'f', 'sGyWr77uikZRpH3QKhtCNw==', '管理员', 1);
INSERT INTO "public"."sys_user" VALUES (6801109150106521602, 'user', '$2a$10$MGU4nqGCr5jqcZ.EHgaOeu3eisYnh4JBRlL5Op92ZoeCuH2YHNc9e', '演示普通用户', '', '', NULL, 0, NULL, 4, 1, NULL, NULL, NULL, 1, '2025-07-19 17:04:44.705721', 1, '2025-07-19 17:04:44.705721', 'f', 'b7cDYnxcsNXfNoPIsFnE2g==', '普通用户', 1);
INSERT INTO "public"."sys_user" VALUES (1, 'superAdmin', '$2a$10$e6XhWnHCECY8XbypNdv8xO1Ts9gALy6OFjy8DVvCvlUVCLzbu4VRS', '系统管理员', 'admin@chaitech.com', '13888888888', NULL, 1, NULL, 3, 1, '2025-07-12 21:36:51.604954', '0:0:0:0:0:0:0:1', '管理员', 1, '2025-07-03 21:15:01.460808', 1, '2025-07-16 22:02:10.300143', 'f', 'YWRtaW5fc2FsdF8yMDI0', '管理员', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
                                          "id" int8 NOT NULL DEFAULT nextval('sys_user_role_id_seq'::regclass),
                                          "user_id" int8 NOT NULL,
                                          "role_id" int8 NOT NULL,
                                          "create_by" int8,
                                          "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_user_role"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_user_role"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1946496307722346498, 6801109150106521601, 2, 1, '2025-07-19 17:04:18.571064');
INSERT INTO "public"."sys_user_role" VALUES (1946496417311121410, 6801109150106521602, 3, 1, '2025-07-19 17:04:44.707234');
INSERT INTO "public"."sys_user_role" VALUES (2, 1, 3, 1, '2025-07-03 21:15:01.460808');

-- ----------------------------
-- Indexes structure for table sys_dept
-- ----------------------------
CREATE INDEX "idx_sys_dept_deleted" ON "public"."sys_dept" USING btree (
    "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_dept_parent_id" ON "public"."sys_dept" USING btree (
    "parent_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_dept_status" ON "public"."sys_dept" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_login_log
-- ----------------------------
CREATE INDEX "idx_sys_login_log_login_time" ON "public"."sys_login_log" USING btree (
    "login_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_login_log_status" ON "public"."sys_login_log" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_login_log_username" ON "public"."sys_login_log" USING btree (
    "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_login_log
-- ----------------------------
ALTER TABLE "public"."sys_login_log" ADD CONSTRAINT "sys_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_menu
-- ----------------------------
CREATE INDEX "idx_sys_menu_deleted" ON "public"."sys_menu" USING btree (
    "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_menu_parent_id" ON "public"."sys_menu" USING btree (
    "parent_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_menu_status" ON "public"."sys_menu" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_menu_type" ON "public"."sys_menu" USING btree (
    "menu_type" "pg_catalog"."int4_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_operation_log
-- ----------------------------
CREATE INDEX "idx_sys_operation_log_oper_name" ON "public"."sys_operation_log" USING btree (
    "oper_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_operation_log_oper_time" ON "public"."sys_operation_log" USING btree (
    "oper_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_operation_log_title" ON "public"."sys_operation_log" USING btree (
    "title" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_operation_log
-- ----------------------------
ALTER TABLE "public"."sys_operation_log" ADD CONSTRAINT "sys_operation_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role
-- ----------------------------
CREATE INDEX "idx_sys_role_code" ON "public"."sys_role" USING btree (
    "role_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_role_deleted" ON "public"."sys_role" USING btree (
    "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_role_status" ON "public"."sys_role" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Uniques structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_role_code_key" UNIQUE ("role_code");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role_dept
-- ----------------------------
CREATE INDEX "idx_sys_role_dept_dept_id" ON "public"."sys_role_dept" USING btree (
    "dept_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_role_dept_role_id" ON "public"."sys_role_dept" USING btree (
    "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role_menu
-- ----------------------------
CREATE INDEX "idx_sys_role_menu_menu_id" ON "public"."sys_role_menu" USING btree (
    "menu_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_role_menu_role_id" ON "public"."sys_role_menu" USING btree (
    "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_user
-- ----------------------------
CREATE INDEX "idx_sys_user_deleted" ON "public"."sys_user" USING btree (
    "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_user_dept_id" ON "public"."sys_user" USING btree (
    "dept_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_user_status" ON "public"."sys_user" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_user_username" ON "public"."sys_user" USING btree (
    "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Uniques structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_username_key" UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_user_role
-- ----------------------------
CREATE INDEX "idx_sys_user_role_role_id" ON "public"."sys_user_role" USING btree (
    "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_user_role_user_id" ON "public"."sys_user_role" USING btree (
    "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "fk_sys_role_dept_dept_id" FOREIGN KEY ("dept_id") REFERENCES "public"."sys_dept" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "fk_sys_role_dept_role_id" FOREIGN KEY ("role_id") REFERENCES "public"."sys_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "fk_sys_role_menu_menu_id" FOREIGN KEY ("menu_id") REFERENCES "public"."sys_menu" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "fk_sys_role_menu_role_id" FOREIGN KEY ("role_id") REFERENCES "public"."sys_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "fk_sys_user_dept_id" FOREIGN KEY ("dept_id") REFERENCES "public"."sys_dept" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "fk_sys_user_role_role_id" FOREIGN KEY ("role_id") REFERENCES "public"."sys_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "fk_sys_user_role_user_id" FOREIGN KEY ("user_id") REFERENCES "public"."sys_user" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
