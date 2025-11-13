-- 添加代码生成器菜单
-- menu_type: 1=目录, 2=菜单, 3=按钮

-- 代码生成器菜单（ID: 6）
INSERT INTO "public"."sys_menu" 
VALUES (
    '6', 
    '1', 
    '代码生成', 
    2, 
    '/system/generator', 
    '/system/generator/GeneratorView', 
    NULL, 
    'DocumentCopy', 
    5, 
    1, 
    1, 
    0, 
    0, 
    '代码生成器菜单', 
    1, 
    CURRENT_TIMESTAMP, 
    NULL, 
    CURRENT_TIMESTAMP, 
    'f', 
    NULL
);

-- 代码生成器按钮权限
-- 查询表列表（ID: 600）
INSERT INTO "public"."sys_menu" 
VALUES (
    '600', 
    '6', 
    '查询表列表', 
    3, 
    '', 
    '', 
    'system:generator:list', 
    NULL, 
    1, 
    1, 
    1, 
    0, 
    0, 
    '', 
    1, 
    CURRENT_TIMESTAMP, 
    NULL, 
    CURRENT_TIMESTAMP, 
    'f', 
    NULL
);

-- 生成代码（ID: 601）
INSERT INTO "public"."sys_menu" 
VALUES (
    '601', 
    '6', 
    '生成代码', 
    3, 
    '', 
    '', 
    'system:generator:generate', 
    NULL, 
    2, 
    1, 
    1, 
    0, 
    0, 
    '', 
    1, 
    CURRENT_TIMESTAMP, 
    NULL, 
    CURRENT_TIMESTAMP, 
    'f', 
    NULL
);

-- 批量生成（ID: 602）
INSERT INTO "public"."sys_menu" 
VALUES (
    '602', 
    '6', 
    '批量生成', 
    3, 
    '', 
    '', 
    'system:generator:batch', 
    NULL, 
    3, 
    1, 
    1, 
    0, 
    0, 
    '', 
    1, 
    CURRENT_TIMESTAMP, 
    NULL, 
    CURRENT_TIMESTAMP, 
    'f', 
    NULL
);

-- 为管理员角色（ID=1）添加代码生成器菜单权限
INSERT INTO "public"."sys_role_menu" ("role_id", "menu_id", "create_by", "create_time")
SELECT '1', '6', 1, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM "public"."sys_role_menu" WHERE "role_id" = '1' AND "menu_id" = '6');

INSERT INTO "public"."sys_role_menu" ("role_id", "menu_id", "create_by", "create_time")
SELECT '1', '600', 1, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM "public"."sys_role_menu" WHERE "role_id" = '1' AND "menu_id" = '600');

INSERT INTO "public"."sys_role_menu" ("role_id", "menu_id", "create_by", "create_time")
SELECT '1', '601', 1, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM "public"."sys_role_menu" WHERE "role_id" = '1' AND "menu_id" = '601');

INSERT INTO "public"."sys_role_menu" ("role_id", "menu_id", "create_by", "create_time")
SELECT '1', '602', 1, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM "public"."sys_role_menu" WHERE "role_id" = '1' AND "menu_id" = '602');

