package es.udc.ws.app.model.register;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRegisterDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRegisterDaoFactory.className";
    private static SqlRegisterDao dao = null;

    private SqlRegisterDaoFactory() {}

    @SuppressWarnings("rawtypes")
    private static SqlRegisterDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRegisterDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlRegisterDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;
    }
}
