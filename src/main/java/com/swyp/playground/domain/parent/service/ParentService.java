package com.swyp.playground.domain.parent.service;

import com.swyp.playground.common.domain.TypeChange;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.dto.req.ParentCreateReqDto;
import com.swyp.playground.domain.parent.dto.res.ParentCreateResDto;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepository parentRepository;
    private final TypeChange typeChange;
    private final PasswordEncoder passwordEncoder;

    public ParentCreateResDto signUp(ParentCreateReqDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Parent parent = typeChange.parentCreateReqDtoToParent(request, encodedPassword);
        Parent savedParent = parentRepository.save(parent);

        return typeChange.parentToParentCreateResDto(savedParent);
    }
}
