package com.pado.chat.application;

import com.pado.chat.aes.AesGcmUtil;
import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingEncryptService {

    private final AesGcmUtil aesUtil;

    public Chatting encrypt(Chatting raw) {
        return new Chatting(raw.getTsid(), aesUtil.encrypt(raw.getMessage()), raw.getSender());
    }

    public Chatting decrypt(Chatting encrypted) {
        return new Chatting(encrypted.getTsid(), aesUtil.decrypt(encrypted.getMessage()), encrypted.getSender());
    }
}
