package com.ajouin.summaryservice.service

import com.aallam.ktoken.Encoding
import com.aallam.ktoken.Tokenizer
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class TokenTest {

    @Test
    @DisplayName("토큰")
    fun tokenNumTest(): Unit = runBlocking {
        val tokenizer = Tokenizer.of(encoding = Encoding.CL100K_BASE)
        val string = "2024 동아리•소학회 등록 안내 2024학년도 동아리·소학회 등록을 실시합니다. 기존 또는 신규 개설을 원하는 학생단체는 정해진 절차에 따라 등록을 진행하여 주십시오. 등록 절차를 모두 마친 동아리•소학회만 활동지원비가 지원되며 공식활동 단체로서 인정됩니다. 2024학년도 학생단체 지원금은 전년도 활동 성과를 평가하여 등급을 나누어 차등적으로 지원될 예정입니다. (단, 신규동아리 및 신규소학회는 지원금이 지급되지 않습니다.) 제출한 자료는 2024학년도 동아리 소학회 콘테스트 심사자료로 활용됩니다. □ 등록대상 - 동아리(정동아리, 준동아리, 신설동아리) - 소학회(전공, 비전공소학회, 신설소학회) □ 등록자: 동아리,소학회 회장(권한 신청을 해야만 전산시스템 AIMS 입력 가능) □ 등록기간 1. 등록 권한 신청 기간: 2024.3.6.(수) 09:30 ~ 2024.3.18.(월) 18:00 - 소학회 권한신청: (https://forms.gle/4Nf5cCuKYJJVigVv7) - 동아리 권한신청: (https://forms.gle/KxjuBekyr2Cg353A6) 2. AIMS 등록 기간: 2024.3.7.(목) 09:30 ~ 2024.3.19.(화) 18:00 □ 등록내용 - AIMS 등록정보 입력(2024.03.07.(목) 부터) - 활동보고서(지정양식) 제출 - 지도교수취임승낙서 제출(비대면 승낙 증빙자료(이메일 등) 인정) 단, 소학회의 경우 지도교수 취임승낙서 필수 제출 ※ 모든 제출물은 PDF파일로 이메일 송부 - 동아리: 학생지원팀 담당자(a10636@ajou.ac.kr)제출 - 소학회: 각 교학팀 담당자에게 메일로 제출(소프트웨어융합대학 공통 소학회 담당자 sw_office@ajou.ac.kr) 각 교학팀 담당자 명단: 붙임파일 2번 참조 □ 유의사항 - 2023학년도 등록 상황을 유지하여 학생단체 등록 진행 구분 2023학년도 상황 등록 비고 학생단체 (동아리및소학회) 미등록 신규 동아리 / 소학회 *미등록(2023)->신규단체(2024) 등록 기존 동아리 / 소학회 □ 2024학년도 지원금 지급 방식 - 2023학년도 활동보고서 심사 후 등급별 지원금액 차등 지급 - 정식 등록절차와 심사를 마친 학생단체(동아리, 소학회) 모두에게 지원금 지급 - 단, 신규 단체와 절차를 완료하지 못하거나 심사에 통과하지 못한 단체의 경우에는 지급 불가 □ 문의: 학생지원팀(031-219-2033) 붙임1. 소학회 학생 신청 방법 안내 붙임2. 2024학년도 소학회 담당자 안내 붙임3. 소학회 AIMS 등록방법 안내(학생용) 붙임4. 동아리 AIMS 등록방법 안내(학생용) 붙임5. 제출서류 양식"
        val tokenNum = tokenizer.encode(string)

        Assertions.assertThat(tokenNum).isNotEmpty()
        Assertions.assertThat(tokenNum.size).isEqualTo(1192)
    }
}