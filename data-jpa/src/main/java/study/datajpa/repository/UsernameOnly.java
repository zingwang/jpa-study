package study.datajpa.repository;

import lombok.Value;

public interface UsernameOnly {

    //@Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
