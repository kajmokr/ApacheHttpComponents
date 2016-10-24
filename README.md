# Apache HttpComponents

#1.	HttpCore: 
A.	Low level의 HTTP 전송 컴포넌트(추상적).
B.	HTTP 기반의 Server와 Client 구현 가능.
C.	framework infrastructure를 정의하여 다른 모듈이 플러그인 할 수 있게 함.
D.	Blocking I/O와 Non-blocking I/O 지원.
i.	Blocking I/O model: 어플리케이션(프로그램)에서 recvfrom(read)를 호출 할 때, 커널은 데이터가 들어올 때까지 봉쇄(blocking)을 시킨다. 데이터가 들어오면 recvfrom(read)함수는 return이 되고, 프로그램 흐름은 어플리케이션(프로그램)으로 돌아온다. recvfrom(read)의 개수만큼의 스레드 생성.
 
①	Java I/O 기반(Java I/O: 파일 시스템의 정보를 얻고 데이터를 입출력하기 위한 다양한 입출력 클래스를 제공하는 패키지).
Java.io 패키지의 주요 클래스	설명
File	파일 시스템의 파일 정보를 얻기 위한 클래스
Console	콘솔로부터 문자를 입출력하기 위한 클래스
InputStream / OutputStream	바이트 단위 입출력을 위한 최상위 입출력 스트림 클래스
FileInputStream / FileOutputStream
DataInputStream/ DataOutputStream
ObejctInputStream/ObjectOutputStream
PrintStream
BufferedInputStream/BufferedOutputStream	바이트 단위 입출력을 위한 하위 스트림 클래스
Reader / Writer	문자 단위 입출력을 위한 최상위 입출력 스트림 클래스
FileReader / FileWriter
InputStreamReader / OutputStreamWriter
PrintWriter
BufferedReader / BufferedWriter	문자 단위 입출력을 위한 하위 스트림 클래스

②	Client 수가 적고, 전송되는 데이터가 대용량이면서 순차적으로 처리될 필요성이 있을 때 사용.
ii.	Non-blocking(event driven) I/O model: 읽을 데이터가 있으면 읽고, 없으면 넘어가게 되는 방법. 
 
①	Java NIO(NIO: New Input/Output) 기반.
NIO 주요패키지	포함되어 있는 내용
java.nio	다양한 버퍼 클래스
java.nio.channels	파일 채널, TCP 채널, UDP 채널 등의 클래스
java.nio.channels.spi	java.nio.channels 패키지를 위한 서비스 제공자 클래스
java.nio.charset	문자셋, 인코더, 디코더 API
java.nio.charset.spi	java.nio.charset 패키지를 위한 서비스 제공자 클래스
java.nio.file	파일 및 파일 클래스에 접근하기 위한 클래스
java.nio.file.attribute	파일 및 파일 클래스의 속성에 접근하기 위한 클래스
java.nio.file.spi	java.nio.file 패키지를 위한 서비스 제공자 클래스

②	스레드 한 개만 생성.
③	핵심 개체인 멀티플렉서(multiplexor)와 셀렉터(Selector)를 활용하여 복수 개의 채널 중에서 준비 완료된 채널을 선택하는 방법을 제공.
④	Client수가 많고, 하나의 입출력 처리작업이 오래 걸리지 않는 경우에 사용.
※	Java IO와 NIO의 비교
	IO	NIO
입출력 방식	스트림 방식(일방향)	채널 방식(양방향)
버퍼 방식	넌버퍼(Non- buffer)	버퍼(Buffer)
비동기 방식	지원 안 함	지원
블로킹 / 넌블로킹 방식	블로킹 방식만 지원	블로킹/넌블로킹 방식 
모두지원
 
E.	인터페이스 구조.
i.	Messages: RequestLine(Request), StatusLine(Response), ProtocolVersion, HttpVersion, Header, HeaderElements, HttpEntity(Body).
ii.	Connections: HttpConnection(Check connection), HttpClientConnection, HttpServerConnection(Actually send and receive). 
iii.	Execution: RequestUserAgent(request interceptor), RequestContent, ResponseContent (Check entity), HttpProcessor(lists of request and response interceptors).
iv.	Parameters: HttpParams(Attachment), PNames(Defining), Bean(Mapping).

#2.	HttpClient: HttpCore를 기반으로 만든 Client-side HTTP 통신 라이브러리.
A.	특징
i.	모든 HTTP 메소드(GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE) 구현.
ii.	Blocking I/O 기반.
iii.	손쉬운 Http Proxy구성이 가능.
※	Http Proxy: 데이터를 가져올 때 해당 사이트에서 바로 자신의 PC로 가져오는 것이 아니라 임시 저장소를 거쳐서 가져오는 것.
iv.	Javascript 실행이 불가능.
v.	Web Browser가 아님
①	UI가 없음.
②	HTTP 통신만 가능.
③	URI Redirect 동작이나 HTML 랜더링이 불가능.
vi.	쿠키생성, HTTP 상태관리, HTTP 연결관리는 가능.
vii.	HTTP 표준 외에는 관대하지 않음.
viii.	스레드의 안정성을 체크.
B.	목적: 반복적인 웹 작업들을 자동화 시키기 위해서.
------------------------------Tutorial 내용(중요도 낮음)--------------------------------
C.	Request execution: HttpClient의 가장 기본적인 기능.
i.	HTTP request: method명, request URI, HTTP protocol version, body로 구성.
ii.	HTTP response: protocol version, numeric status code, textual phrase로 구성.
iii.	HTTP entity: 메시지의 내용을 포함하는 개체.
①	Streamed: The content is received from a stream, or generated on the fly.
②	Self-contained: The content is in memory or obtained by means that are independent from a connection or other entity.
③	Wrapping: The content is obtained from another entity.
iv.	Ensuring release of low level resources
①	Closing the content stream: Attempt to keep the underlying connection alive by consuming the entity content 
②	Closing the response: immediately shuts down and discards the connection.
v.	Consuming entity content: HttpEntity#getContent(),HttpEntity#getContent()
vi.	Producing entity content:  stream out content throught HTTP connections.
vii.	Response handlers: Automatically take care of ensuring release of the connection back to the connection manager
D.	HttpClient interface: enables the users to selectively replace default implementation of those aspects with custom, application specific ones.
E.	HTTP execution context: maintain a processing state HttpClient allows HTTP requests to be executed within a particular execution context.
i.	HttpConnection: the actual connection to the target server.
ii.	HttpHost: the connection target.
iii.	HttpRoute: the complete connection route
iv.	HttpRequest: the actual HTTP request. The final HttpRequest object in the execution context always represents the state of the message exactly as it was sent to the target server. Per default HTTP/1.0 and HTTP/1.1 use relative request URIs. However if the request is sent via a proxy in a non-tunneling mode then the URI will be absolute.
v.	HttpResponse: the actual HTTP response.
vi.	java.lang.Boolean: the flag indicating whether the actual request has been fully transmitted to the connection target.
vii.	RequestConfig: the actual request configuation.
viii.	java.util.List<URI>: a collection of all redirect locations received in the process of request execution.

-------------------------------------------------------------------------------------------
#3.	Asynch HttpClient: HttpCore NIO를 이용하여 구현한 HTTP 기반의 Client.
A.	Non-Blocking I/O 기반.
B.	A complementary module to HttpClient 
C.	Intended for special cases where ability to handle a great number of concurrent connections is more important than performance in terms of a raw data throughput.
