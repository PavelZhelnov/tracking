package com.tmw.tracking.dao;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pzhelnov on 2/21/2017.
 */
@Category(TrackingBaseUnitTest.class)
public class RoleDaoTest extends TrackingBaseUnitTest {

    private RoleDao roleDao;
    private PermissionDao permissionDao;

    private final static Logger logger = LoggerFactory.getLogger(RoleDaoTest.class);

        /**
         * {@inheritDoc}
         * @see TrackingBaseUnitTest#setUp()
         */
        @Before
        public void setUp() throws Exception {
            super.setUp();
            roleDao = injector.getInstance(RoleDao.class);
            permissionDao = injector.getInstance(PermissionDao.class);
        }

        @Test
        public void testCRUDForRole() {
            Role role = new Role();
            role.setRoleName("Test");
            Set<Permission> permissionList = new HashSet<Permission>();
            Permission permission = permissionDao.getByPermissionType(PermissionType.SHOW_DICTIONARIES);
            assertNotNull(permission);
            permissionList.add(permission);
            role.setPermissionList(permissionList);

            Role updated = roleDao.update(role);
            assertNotNull(updated.getId());

            Role fromDataBase = roleDao.getById(updated.getId());
            assertNotNull(fromDataBase);
            assertNotNull(fromDataBase.getPermissionList());
            assertTrue(fromDataBase.getPermissionList().size() == 1);

            //delete
            roleDao.delete(updated);

            Role deleted = roleDao.getById(updated.getId());
            assertNull(deleted);
        }


    }
