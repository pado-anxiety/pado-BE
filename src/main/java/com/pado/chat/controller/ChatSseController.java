package com.pado.chat.controller;

import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.chat.application.AIChatFacade;
import com.pado.chat.controller.dto.message.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatSseController {

    private final AIChatFacade AIChatFacade;

    @PostMapping(value = "/v2/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter send(@LoginUser AuthUser authUser, @RequestBody MessageRequest request) {
        SseEmitter emitter = new SseEmitter(300_000L);

        Flux<String> flux = AIChatFacade.postMessageV2(authUser.getUserId(), request)
                .publishOn(Schedulers.boundedElastic()); //epoll -> bounded

        Disposable disposable = flux.subscribe(
                chunk -> {
                    try {
                        emitter.send(SseEmitter.event().data(chunk));
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                },
                emitter::completeWithError,
                emitter::complete
        ); //emit thread = bounded

        emitter.onCompletion(disposable::dispose);
        emitter.onTimeout(() -> {
            disposable.dispose();
            emitter.complete();
        });
        emitter.onError(e -> disposable.dispose());

        return emitter;
    }
}
