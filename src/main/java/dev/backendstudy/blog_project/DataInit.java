package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.MemberRole;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "data-init.enabled", havingValue = "true")
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {
    private static final int NOTICE_COUNT = 3;
    private static final int POSTS_PER_CATEGORY = 20;

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (memberRepository.existsByLoginId("admin")) {
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member admin = memberRepository.save(
                new Member("관리자", "admin", encoder.encode("1"), MemberRole.ADMIN)
        );

        Member user = memberRepository.save(
                new Member("종빈쓰", "1", encoder.encode("1"))
        );

        admin.updateProfile(admin.getUsername(), "/images/dummy/admin-profile.jpg");
        user.updateProfile(user.getUsername(), "/images/dummy/user-profile.jpg");

        Board firstBoard = createNotices(admin);
        createCategoryPosts(user);

        replyRepository.save(new Reply("비밀 댓글입니다.", user, firstBoard));
        replyRepository.save(new Reply("기본 댓글입니다.", admin, firstBoard));
    }

    private Board createNotices(Member admin) {
        Board firstBoard = null;

        for (int i = 1; i <= NOTICE_COUNT; i++) {
            Board board = boardRepository.save(
                    new Board("공지사항", "공지사항입니다. 게시판 이용 규칙과 주요 안내를 확인해주세요.", admin)
            );
            board.markAsNotice();
            boardRepository.updateDummyFields(
                    board.getId(),
                    LocalDateTime.now().minusHours(NOTICE_COUNT - i),
                    randomLong(100, 1000),
                    randomLong(20, 200),
                    randomLong(0, 20)
            );

            if (i == 1) {
                firstBoard = board;
            }
        }

        return firstBoard;
    }

    private void createCategoryPosts(Member user) {
        List<TopicGroup> groups = List.of(
                new TopicGroup("Network", List.of(
                        new Topic("OSI 7 Layer", "네트워크 통신 과정을 물리 계층부터 애플리케이션 계층까지 나누어 이해하는 기준입니다."),
                        new Topic("TCP 3-Way Handshake", "TCP 연결을 시작할 때 SYN, SYN-ACK, ACK로 세션을 확립하는 과정입니다."),
                        new Topic("TCP 4-Way Handshake", "연결 종료 시 FIN과 ACK를 주고받아 양방향 세션을 정리하는 과정입니다."),
                        new Topic("UDP", "연결 설정 없이 빠르게 데이터를 보내지만 순서와 재전송을 보장하지 않는 전송 계층 프로토콜입니다."),
                        new Topic("HTTP와 HTTPS", "HTTP는 웹 요청/응답 프로토콜이고 HTTPS는 TLS로 암호화와 인증을 더한 방식입니다."),
                        new Topic("DNS", "도메인 이름을 IP 주소로 변환해 사용자가 서버에 접근할 수 있게 해주는 시스템입니다."),
                        new Topic("IP와 Subnet", "IP 주소와 서브넷 마스크를 이용해 네트워크 영역과 호스트 영역을 구분합니다."),
                        new Topic("NAT", "사설 IP와 공인 IP를 변환해 내부 네트워크가 외부와 통신하도록 돕는 기술입니다."),
                        new Topic("Gateway", "서로 다른 네트워크로 패킷을 전달할 때 거치는 기본 출구 역할의 장비입니다."),
                        new Topic("Router와 Switch", "라우터는 네트워크 간 경로를 결정하고 스위치는 같은 네트워크 안에서 프레임을 전달합니다."),
                        new Topic("ARP", "같은 네트워크에서 IP 주소에 대응하는 MAC 주소를 찾기 위한 프로토콜입니다."),
                        new Topic("ICMP", "ping처럼 네트워크 상태나 오류 정보를 전달하는 데 사용되는 제어 메시지 프로토콜입니다."),
                        new Topic("TLS", "인증서와 키 교환을 통해 애플리케이션 데이터의 기밀성과 무결성을 보호합니다."),
                        new Topic("CORS", "브라우저가 다른 출처의 리소스 요청을 허용할지 판단하는 보안 정책입니다."),
                        new Topic("Cookie와 Session", "쿠키는 클라이언트에 저장되는 값이고 세션은 서버가 로그인 상태 등을 관리하는 방식입니다."),
                        new Topic("REST API", "자원을 URI로 표현하고 HTTP 메서드로 행위를 구분하는 API 설계 방식입니다."),
                        new Topic("WebSocket", "HTTP 핸드셰이크 이후 하나의 연결로 양방향 실시간 통신을 제공하는 프로토콜입니다."),
                        new Topic("Load Balancer", "여러 서버로 요청을 분산해 가용성과 처리량을 높이는 구성 요소입니다."),
                        new Topic("Proxy", "클라이언트와 서버 사이에서 요청을 대신 전달하거나 캐싱, 필터링을 수행합니다."),
                        new Topic("CDN", "정적 리소스를 사용자와 가까운 엣지 서버에서 제공해 응답 속도를 개선합니다.")
                )),
                new TopicGroup("OS", List.of(
                        new Topic("Process와 Thread", "프로세스는 실행 중인 프로그램 단위이고 스레드는 프로세스 안에서 실행되는 작업 흐름입니다."),
                        new Topic("Context Switching", "CPU가 실행 대상을 바꾸기 위해 레지스터와 실행 상태를 저장하고 복원하는 과정입니다."),
                        new Topic("Scheduling", "운영체제가 CPU를 어떤 프로세스나 스레드에 배정할지 결정하는 정책입니다."),
                        new Topic("Race Condition", "공유 자원 접근 순서에 따라 실행 결과가 달라지는 동시성 문제입니다."),
                        new Topic("Critical Section", "여러 스레드가 동시에 접근하면 안 되는 공유 자원 접근 구간입니다."),
                        new Topic("Mutex", "한 번에 하나의 스레드만 임계 구역에 들어가도록 제한하는 동기화 도구입니다."),
                        new Topic("Semaphore", "정해진 개수만큼 스레드가 공유 자원에 접근하도록 제어하는 동기화 방식입니다."),
                        new Topic("Deadlock", "여러 작업이 서로 가진 자원을 기다리며 영원히 진행하지 못하는 상태입니다."),
                        new Topic("Starvation", "특정 작업이 자원을 계속 얻지 못해 실행 기회를 잃는 현상입니다."),
                        new Topic("Memory Management", "운영체제가 프로세스에 메모리를 할당하고 회수하며 보호하는 기능입니다."),
                        new Topic("Virtual Memory", "디스크 일부를 메모리처럼 활용해 실제 물리 메모리보다 큰 주소 공간을 제공하는 기법입니다."),
                        new Topic("Paging", "메모리를 고정 크기 페이지로 나누어 관리하는 가상 메모리 구현 방식입니다."),
                        new Topic("Segmentation", "코드, 데이터, 스택처럼 의미 단위로 메모리 영역을 나누어 관리하는 방식입니다."),
                        new Topic("Page Fault", "필요한 페이지가 메모리에 없어 디스크에서 읽어와야 할 때 발생하는 예외입니다."),
                        new Topic("Cache Locality", "최근 사용한 데이터나 가까운 주소의 데이터를 다시 사용할 가능성이 높다는 성질입니다."),
                        new Topic("System Call", "사용자 프로그램이 파일, 네트워크, 프로세스 같은 커널 기능을 요청하는 인터페이스입니다."),
                        new Topic("Kernel과 User Mode", "커널 모드는 시스템 자원에 직접 접근하고 유저 모드는 제한된 권한으로 실행됩니다."),
                        new Topic("File System", "파일과 디렉터리를 저장 장치에 구조화해 저장하고 조회하는 운영체제 구성 요소입니다."),
                        new Topic("Interrupt", "하드웨어나 소프트웨어 이벤트가 CPU의 흐름을 잠시 중단시키고 처리 루틴을 실행하게 합니다."),
                        new Topic("I/O Blocking", "입출력 작업이 끝날 때까지 스레드가 대기하는 실행 방식입니다.")
                )),
                new TopicGroup("DB", List.of(
                        new Topic("Index", "테이블 검색 속도를 높이기 위해 특정 컬럼 값을 별도 자료구조로 관리하는 기능입니다."),
                        new Topic("B-Tree", "DB 인덱스에 많이 쓰이며 정렬된 데이터를 균형 있게 유지해 탐색 비용을 줄이는 트리입니다."),
                        new Topic("Transaction", "여러 DB 작업을 하나의 논리적 작업 단위로 묶어 처리하는 개념입니다."),
                        new Topic("ACID", "트랜잭션이 원자성, 일관성, 격리성, 지속성을 만족해야 한다는 원칙입니다."),
                        new Topic("Isolation Level", "동시에 실행되는 트랜잭션이 서로의 변경을 어느 정도 볼 수 있는지 정하는 수준입니다."),
                        new Topic("Dirty Read", "아직 커밋되지 않은 다른 트랜잭션의 변경 값을 읽는 현상입니다."),
                        new Topic("Non-Repeatable Read", "같은 행을 두 번 읽었는데 중간 커밋 때문에 값이 달라지는 현상입니다."),
                        new Topic("Phantom Read", "같은 조건으로 조회했는데 중간 삽입이나 삭제로 결과 행 집합이 달라지는 현상입니다."),
                        new Topic("Lock", "동시 접근 중 데이터 정합성을 지키기 위해 읽기나 쓰기를 제한하는 장치입니다."),
                        new Topic("Optimistic Lock", "충돌이 드물다고 보고 버전 값을 비교해 갱신 충돌을 감지하는 방식입니다."),
                        new Topic("Pessimistic Lock", "충돌 가능성이 높다고 보고 데이터를 먼저 잠근 뒤 작업하는 방식입니다."),
                        new Topic("Normalization", "중복을 줄이고 이상 현상을 막기 위해 테이블을 정규형에 맞게 분리하는 과정입니다."),
                        new Topic("Join", "여러 테이블의 관련 데이터를 조건에 맞춰 하나의 결과로 결합하는 연산입니다."),
                        new Topic("N+1 Problem", "연관 데이터를 조회할 때 추가 쿼리가 반복 실행되어 성능이 떨어지는 문제입니다."),
                        new Topic("Query Plan", "DB 옵티마이저가 SQL을 어떤 순서와 방법으로 실행할지 선택한 계획입니다."),
                        new Topic("Primary Key", "테이블에서 각 행을 유일하게 식별하는 컬럼 또는 컬럼 조합입니다."),
                        new Topic("Foreign Key", "다른 테이블의 기본키를 참조해 테이블 간 관계와 무결성을 표현합니다."),
                        new Topic("Replication", "데이터를 다른 DB 서버에 복제해 읽기 확장성과 장애 대응력을 높이는 구성입니다."),
                        new Topic("Sharding", "대량 데이터를 기준에 따라 여러 DB에 나누어 저장하는 수평 분할 방식입니다."),
                        new Topic("Connection Pool", "DB 연결을 미리 만들어 재사용해 연결 생성 비용과 지연을 줄이는 기법입니다.")
                )),
                new TopicGroup("Algorithm", List.of(
                        new Topic("Time Complexity", "입력 크기가 커질 때 알고리즘 실행 시간이 어떻게 증가하는지 나타내는 척도입니다."),
                        new Topic("Space Complexity", "알고리즘이 입력 외에 추가로 사용하는 메모리 양을 분석하는 기준입니다."),
                        new Topic("Big-O", "상수와 낮은 차수를 제외하고 최악 또는 상한 증가율을 표현하는 표기법입니다."),
                        new Topic("Binary Search", "정렬된 배열에서 중간 값을 기준으로 탐색 범위를 절반씩 줄이는 알고리즘입니다."),
                        new Topic("DFS", "그래프나 트리에서 한 경로를 깊게 탐색한 뒤 되돌아오는 방식입니다."),
                        new Topic("BFS", "시작점에서 가까운 정점부터 계층별로 탐색하는 그래프 탐색 방식입니다."),
                        new Topic("Recursion", "함수가 자기 자신을 호출해 문제를 더 작은 문제로 나누어 해결하는 방식입니다."),
                        new Topic("Backtracking", "가능한 선택을 탐색하다 조건에 맞지 않으면 이전 상태로 돌아가는 기법입니다."),
                        new Topic("Dynamic Programming", "중복되는 부분 문제의 결과를 저장해 전체 문제를 효율적으로 해결하는 방법입니다."),
                        new Topic("Greedy", "각 단계에서 현재 가장 좋아 보이는 선택을 하며 해답을 구성하는 전략입니다."),
                        new Topic("Dijkstra", "음수 가중치가 없는 그래프에서 시작점부터 각 정점까지 최단 거리를 구하는 알고리즘입니다."),
                        new Topic("Floyd-Warshall", "모든 정점 쌍 사이의 최단 거리를 동적 계획법으로 구하는 알고리즘입니다."),
                        new Topic("Union-Find", "서로소 집합을 빠르게 합치고 같은 집합인지 확인하는 자료구조입니다."),
                        new Topic("Topological Sort", "방향 비순환 그래프에서 선후 관계를 만족하는 순서를 만드는 알고리즘입니다."),
                        new Topic("Heap", "최댓값이나 최솟값을 빠르게 꺼내기 위해 사용하는 완전 이진 트리 기반 자료구조입니다."),
                        new Topic("Hash Table", "키를 해시 함수로 배열 위치에 매핑해 평균적으로 빠른 조회를 제공하는 자료구조입니다."),
                        new Topic("Trie", "문자열을 문자 단위 트리로 저장해 접두사 검색을 빠르게 처리하는 자료구조입니다."),
                        new Topic("Two Pointer", "두 인덱스를 이동시키며 배열이나 문자열의 조건을 효율적으로 확인하는 기법입니다."),
                        new Topic("Sliding Window", "연속 구간을 유지하며 시작과 끝을 이동해 부분 배열 문제를 해결하는 기법입니다."),
                        new Topic("Minimum Spanning Tree", "모든 정점을 연결하면서 간선 가중치 합이 최소가 되는 트리입니다.")
                )),
                new TopicGroup("Spring", List.of(
                        new Topic("IoC Container", "객체 생성과 의존성 관리를 개발자 코드 대신 Spring 컨테이너가 담당하는 구조입니다."),
                        new Topic("DI", "필요한 의존 객체를 직접 만들지 않고 외부에서 주입받아 결합도를 낮추는 방식입니다."),
                        new Topic("Bean", "Spring 컨테이너가 생성하고 생명주기를 관리하는 객체입니다."),
                        new Topic("Component Scan", "지정한 패키지에서 컴포넌트 어노테이션이 붙은 클래스를 찾아 Bean으로 등록합니다."),
                        new Topic("Controller", "웹 요청을 받아 서비스 호출과 응답 생성을 연결하는 MVC 계층입니다."),
                        new Topic("Service", "비즈니스 규칙과 트랜잭션 흐름을 담당하는 애플리케이션 계층입니다."),
                        new Topic("Repository", "DB 접근 로직을 캡슐화하고 JPA 예외 변환 같은 기능을 제공하는 계층입니다."),
                        new Topic("Entity", "DB 테이블과 매핑되는 JPA 도메인 객체입니다."),
                        new Topic("DTO", "계층 간 데이터 전달을 위해 필요한 필드만 담는 객체입니다."),
                        new Topic("Spring MVC", "DispatcherServlet을 중심으로 요청을 컨트롤러, 뷰, 모델로 처리하는 웹 프레임워크입니다."),
                        new Topic("DispatcherServlet", "Spring MVC에서 모든 요청을 받아 적절한 핸들러로 전달하는 프론트 컨트롤러입니다."),
                        new Topic("Handler Mapping", "요청 URL과 HTTP 메서드에 맞는 컨트롤러 메서드를 찾는 구성 요소입니다."),
                        new Topic("Validation", "요청 데이터가 필수 값, 길이, 형식 같은 제약 조건을 만족하는지 검증하는 기능입니다."),
                        new Topic("Exception Handler", "컨트롤러에서 발생한 예외를 공통 응답으로 변환해 처리하는 방법입니다."),
                        new Topic("Transaction", "서비스 메서드 단위로 DB 작업의 커밋과 롤백 경계를 관리하는 기능입니다."),
                        new Topic("Spring Data JPA", "Repository 인터페이스만으로 기본 CRUD와 쿼리 메서드를 제공하는 모듈입니다."),
                        new Topic("Lazy Loading", "연관 엔티티를 실제 사용할 때까지 조회를 미루는 JPA 로딩 전략입니다."),
                        new Topic("AOP", "로깅, 트랜잭션처럼 공통 관심사를 핵심 로직과 분리해 적용하는 방식입니다."),
                        new Topic("Filter와 Interceptor", "Filter는 서블릿 앞단에서, Interceptor는 Spring MVC 핸들러 전후에서 요청을 가로챕니다."),
                        new Topic("Profile", "local, test, prod처럼 실행 환경별 설정을 분리해 선택적으로 적용하는 기능입니다.")
                ))
        );

        int globalIndex = 0;

        for (TopicGroup group : groups) {
            for (int i = 0; i < group.topics().size(); i++) {
                globalIndex++;
                Topic topic = group.topics().get(i);
                Board board = boardRepository.save(
                        new Board(createTitle(group.name(), globalIndex, topic.term()), createContent(group.name(), topic), user)
                );

                boardRepository.updateDummyFields(
                        board.getId(),
                        createdAtBySequence(globalIndex),
                        randomLong(0, 1000),
                        randomLong(0, 200),
                        randomLong(0, 50)
                );
            }
        }
    }

    private String createTitle(String category, int index, String term) {
        return "[%s] %03d. %s 정리".formatted(category, index, term);
    }

    private String createContent(String category, Topic topic) {
        return """
                [%s] %s

                %s

                면접이나 CS 학습에서는 이 용어가 어떤 문제를 해결하는지, 장점과 한계가 무엇인지,
                실제 백엔드 개발에서 어디에 쓰이는지를 함께 정리하면 좋습니다.
                """.formatted(category, topic.term(), topic.description());
    }

    private LocalDateTime createdAtBySequence(int sequence) {
        return LocalDateTime.now()
                .minusDays(100L - sequence)
                .withHour(9)
                .withMinute(sequence % 60)
                .withSecond(0)
                .withNano(0);
    }

    private long randomLong(long minInclusive, long maxInclusive) {
        return ThreadLocalRandom.current().nextLong(minInclusive, maxInclusive + 1);
    }

    private record TopicGroup(String name, List<Topic> topics) {
    }

    private record Topic(String term, String description) {
    }
}
