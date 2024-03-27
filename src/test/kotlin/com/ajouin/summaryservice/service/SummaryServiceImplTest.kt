package com.ajouin.summaryservice.service

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.Content
import com.ajouin.summaryservice.dto.Summary
import com.ajouin.summaryservice.dto.SummaryRequest
import com.ajouin.summaryservice.logger
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SummaryServiceTest @Autowired constructor(
    private val summaryService: SummaryService,
) {

    @DisplayName("openAI API 동작 확인")
    @Test
    fun summaryServiceTest(): Unit = runBlocking {
        val summaryRequest = SummaryRequest(
            id = 0L,
            content = "2024학년도 1학기 가족장학 신청을 \n" +
                    "아래와 같이 안내드리오니 대상 학생은 기간 내 신청하시기 바랍니다.\n" +
                    " 1. 신청자격\n" +
                    " ‣ 직계가족 중 아주대학교 학부과정 수업연한 내 재학 중인 자\n" +
                    " ‣ 또는, 동학년도에 2명 이상 동시에 입학한 자\n" +
                    " 2. 수혜사항 및 수혜대상\n" +
                    " ‣ 입학년도가 나중인 자에게 1개 학기 수업료 전액 면제 (재학기간 중 1회에 한하여 지급)\n" +
                    "ex1) 기존 학부과정 학생이 있는 경우 신규 입학자 지급\n" +
                    "         ex2) 동학년도에 2명 이상 동시 입학 시 연소자 지급\n" +
                    "‣ 가족장학은 수업료성 장학이므로 수업료 잔여 범위내에서 지원\n" +
                    "    ex1) 등록금이 3,000,000원일 때 등록 시 국가장학금을 1,000,000원 받았다면 잔여 2,000,000원을 가족장학으로 지원\n" +
                    " 3. 수혜조건\n" +
                    " ‣ 기존 학생의 경우 휴학, 재학 모두 가능하나, 수혜 대상자는 반드시 재학중이어야 함. (장학금 수령 후, 해당 학기 휴학할 경우 장학금은 반드시 반환해야함)\n" +
                    " ‣ 수혜 대상자는 직전학기 12학점 이상, 2.0이상 성적 취득 (장학기본조건 적용)\n" +
                    " 단, 신입생 및 편입학 신입생의 경우, 입학학기에 한하여 취득학점 및 성적조건 없음.\n" +
                    " 4. 신청기한 : 2024. 3. 27(수) 17:00까지\n" +
                    " ‣ 우편접수 시, 우편물 도착 기준이 2024. 3. 27(수) 17:00까지이므로 미리 처리 요망 \n" +
                    " 5. 필요서류 (총 7개)\n" +
                    " ‣ 신청서 1부\n" +
                    " ‣ 가족관계증명서 (부 또는 모 기준) 1부\n" +
                    " (* 가족관계증명서의 경우 부 또는 모 기준으로 발급받아야 형제증명이 가능)\n" +
                    " ‣ 기존 학생 재학증명서 (휴학일 경우, 재적증명서) 1부\n" +
                    " ‣ 수혜 대상자 재학증명서 1부\n" +
                    " ‣ 수혜 대상자 통장 사본 1부\n" +
                    " ‣ 기존 학생 개인(신용)정보 수집 이용 제공 및 조회 동의서 1부\n" +
                    "‣ 수혜 대상자 개인(신용)정보 수집 이용 제공 및 조회 동의서 1부\n" +
                    " 6. 접수처 \n" +
                    " ‣ 우편접수 또는 내방접수: 학생지원팀(신학생회관 104호)\n" +
                    " ‣ e-mail 접수: alsgml9300@ajou.ac.kr\n" +
                    " 7. 신청방법 : 우편접수 / 내방접수 / e-mail 접수 \n" +
                    " ‣ e-mail 접수시, 필요서류를 스캔(pdf파일전환)하여 e-mail(alsgml9300@ajou.ac.kr)제출\n" +
                    " ‣ 구글플레이 / 앱스토어 PDF 스캔 앱 사용 가능\n" +
                    " ‣ 접수처(내방)주소:\n" +
                    " 우) 16499 경기도 수원시 영통구 월드컵로 206 아주대학교 신학생회관 104호 학생지원팀 교내장학 담당자 앞(031-219-2038)\n" +
                    " ‣ 간혹 누락되는 경우가 있으니, 접수 후 교내장학 담당자(031-219-2038)에게 확인 요망\n" +
                    " 8. 장학금 지급 방법 : 현금 지급(4월 둘째 주 혹은 셋째 주 지급 예정)\n" +
                    " 9. 장학계좌 미입력자 입력 \n" +
                    " ‣ 홈페이지 로그인 -> 학부(학사) -> 학적기본조회 -> 신상 탭 -> 계좌정보 -> 입력/저장\n" +
                    " ‣ 장학계좌 미입력시 지급 불가\n" +
                    " 10. 문의 : 학생지원팀 교내장학 담당자(031-219-2038) / alsgml9300@ajou.ac.kr"
        )
        val content: ChatCompletion = summaryService.summaryContent(summaryRequest)
        val summaryResult: Content? = content.choices[0].message.messageContent

        val gson = Gson()
        val contentJson = gson.toJson(summaryResult)
        val summaryJson = JsonParser.parseString(contentJson).asJsonObject.get("content").asString
        val summary = gson.fromJson(summaryJson, Summary::class.java)

        assertThat(summary.firstSentence).isNotBlank()
        assertThat(summary.secondSentence).isNotBlank()
        assertThat(summary.thirdSentence).isNotBlank()

    }
}