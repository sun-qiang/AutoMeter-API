FROM nginx:1.19.6
ENV TZ=Asia/Shanghai
USER root
COPY dist/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
