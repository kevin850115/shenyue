package com.local.kevin.dal.persistence.impl;

import com.local.kevin.dal.dao.IUserDAO;
import com.local.kevin.dal.model.User;
import com.local.kevin.dal.persistence.KevinPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by kevin on 15/3/8.
 */
@Slf4j
@Service
public class KevinPersistenceImpl implements KevinPersistence{
    @Resource
    protected IUserDAO userDAO;

    @Override
    public User getUserById(User user) {
        return userDAO.retrieveUserById(user.getId());
    }

}
