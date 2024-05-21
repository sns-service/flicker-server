## Description
- 유저 서버입니다.
- EKS(쿠버네티스) sns cluster 내부에 존재합니다.
<br>

## 역할 ✏️
- 유저 회원가입, 로그인을 담당합니다.
- Follow 기능 구현
- 소셜 피드 서버와 통신하여 피드 조회 시 유저 정보를 제공합니다.
<br>

## kubernetes 빌드 및 배포 ✅
- jib 을 통해 AWS ECR 에 이미지를 배포합니다.

![스크린샷 2024-05-21 오후 8 09 20](https://github.com/sns-service/user-server/assets/56336436/e7b4509c-3749-4ac1-969d-59dc0e47c975)

- 루트 폴더의 deploy.yaml 을 실행해 배포합니다.
> kubectl apply -f user-deploy.yaml
