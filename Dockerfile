# ===== Stage 1: Build =====
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Gradle 설정 파일 먼저 복사 (의존성 캐싱 최적화)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 줄 끝 정규화 (Windows CRLF → LF) + 실행 권한
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# 의존성 다운로드 (소스 변경 없으면 이 레이어 캐시 재사용)
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 + 빌드
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 빌드 산출물만 가져오기
COPY --from=builder /build/build/libs/*.jar app.jar

# JVM 옵션 + 프로파일은 환경변수로 (하드코딩 제거)
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS:-} -jar /app/app.jar"]