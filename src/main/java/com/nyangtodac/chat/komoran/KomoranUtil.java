package com.nyangtodac.chat.komoran;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.List;

public class KomoranUtil {

    private static final Komoran KOMORAN = new Komoran(DEFAULT_MODEL.LIGHT);

    // MAG: 부사, NNG: 일반명사, VA: 형용사, VV: 동사
    private static final List<String> EXTRACT_POS = List.of("MAG", "NNG", "VA", "VV");

    public static List<String> analyzeMessage(String message) {
        return KOMORAN.analyze(message).getTokenList().stream().filter(t -> EXTRACT_POS.contains(t.getPos())).map(Token::getMorph).toList();
    }
}
