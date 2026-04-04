package com.pado.quiz.data.gad;

import com.pado.quiz.dto.Choice;
import com.pado.quiz.dto.Question;
import com.pado.quiz.dto.ResultData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizData {

    public List<Question> getQuestions() {
        return List.of(
                new Question(1, "기본 불안감",
                        "평범한 하루, 특별한 일도 없는데 갑자기 불안한 느낌이 밀려온다. 나는?",
                        List.of(
                                new Choice("그런 경험이 별로 없다", 0),
                                new Choice("가끔 그럴 때 있지만 금방 지나간다", 1),
                                new Choice("꽤 자주 그렇고, 이유를 찾으려 한다", 2),
                                new Choice("거의 매일 그래서 익숙하다", 3)
                        )
                ),
                new Question(2, "걱정 통제력",
                        "자기 전에 내일 걱정을 그만하고 싶은데, 머릿속에서 생각이 멈추지 않는다. 나는?",
                        List.of(
                                new Choice("생각을 정리하고 내려놓을 수 있다", 0),
                                new Choice("좀 걸리지만 억지로 멈추고 잠든다", 1),
                                new Choice("멈추려 해도 잘 안 돼서 한참 뒤척인다", 2),
                                new Choice("아무리 해도 멈출 수가 없다", 3)
                        )
                ),
                new Question(3, "과잉 걱정",
                        "일, 관계, 돈, 건강… 이것저것 여러 가지가 한꺼번에 걱정되던 날이 있었나요?",
                        List.of(
                                new Choice("거의 없다", 0),
                                new Choice("가끔 그런 날이 있다", 1),
                                new Choice("자주 그렇다. 하나가 다른 걱정으로 번진다", 2),
                                new Choice("거의 매일 여러 걱정이 동시에 몰려온다", 3)
                        )
                ),
                new Question(4, "이완 불능",
                        "오랜만에 아무 약속 없는 주말이다. 나는?",
                        List.of(
                                new Choice("마음 편히 쉰다", 0),
                                new Choice("쉬긴 하는데 뭔가 해야 할 것 같은 느낌이 든다", 1),
                                new Choice("이것저것 하다가 결국 제대로 쉬지 못한다", 2),
                                new Choice("가만히 있으면 오히려 더 불안해서 억지로 뭔가를 한다", 3)
                        )
                ),
                new Question(5, "과민 반응",
                        "상대방이 한 말이 별거 아닌데도 나도 모르게 날카롭게 받아친 적이 있나요?",
                        List.of(
                                new Choice("거의 없다", 0),
                                new Choice("가끔 그런 적 있지만, 바로 알아차린다", 1),
                                new Choice("종종 그래서 후회한 적이 있다", 2),
                                new Choice("자주 그래서 관계가 힘들어진 적이 있다", 3)
                        )
                ),
                new Question(6, "신체 초조",
                        "몸은 앉아 있어도 마음이 계속 들썩이거나 가만히 있기 힘든 느낌이 든 적이 있나요?",
                        List.of(
                                new Choice("거의 없다", 0),
                                new Choice("가끔 그럴 때가 있다", 1),
                                new Choice("꽤 자주, 다리를 떨거나 뭔가를 만지작거린다", 2),
                                new Choice("거의 항상 그래서 집중이 어렵다", 3)
                        )
                ),
                new Question(7, "파국적 사고",
                        "가까운 사람에게 전화했는데 안 받는다. 나는?",
                        List.of(
                                new Choice("바쁜가 보다, 나중에 다시 한다", 0),
                                new Choice("좀 신경 쓰이지만 기다린다", 1),
                                new Choice("혹시 무슨 일 생긴 건 아닌지 걱정된다", 2),
                                new Choice("최악의 상황이 머릿속을 맴돌아서 계속 전화한다", 3)
                        )
                )
        );
    }

    public ResultData getResult(int score) {
        if (score <= 4) return new ResultData(
                "normal", "0~4점", "안정적인 상태",
                "현재 불안 수준이 낮고, 일상에서 잘 대처하고 있어요.",
                "지금처럼 자신을 잘 돌보고 있다면 괜찮아요. 하지만 가끔 마음을 점검하는 습관은 도움이 돼요."
        );
        if (score <= 9) return new ResultData(
                "mild", "5~9점", "가벼운 불안 신호",
                "일상에서 불안이 살짝 고개를 들고 있어요. 아직은 괜찮지만, 방치하면 커질 수 있어요.",
                "지금이 관리를 시작하기 가장 좋은 타이밍이에요. 작은 습관 하나가 큰 차이를 만들어요."
        );
        if (score <= 14) return new ResultData(
                "moderate", "10~14점", "불안이 일상에 영향을 주고 있어요",
                "걱정이 자주 밀려오고, 쉬는 시간에도 마음이 편하지 않은 상태예요.",
                "혼자 감당하지 않아도 돼요. 체계적인 불안 관리가 지금의 당신에게 필요해요."
        );
        return new ResultData(
                "severe", "15~21점", "지금, 당신의 마음이 도움을 요청하고 있어요",
                "불안이 일상 깊숙이 자리 잡고 있어요. 잠, 관계, 집중력 모두 영향을 받고 있을 수 있어요.",
                "당신 잘못이 아니에요. 지금 느끼는 감정은 자연스러운 거예요. 다만, 전문적인 도움이 큰 힘이 될 수 있어요."
        );
    }
}