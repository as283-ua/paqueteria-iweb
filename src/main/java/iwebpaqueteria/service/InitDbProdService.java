package iwebpaqueteria.service;

import iwebpaqueteria.util.InitDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Profile("!dev")
public class InitDbProdService {
    @Autowired
    private InitDbUtil initDbUtil;

    @PostConstruct
    @Transactional
    public void initDatabase() {
        initDbUtil.initDatabase();
    }

}
