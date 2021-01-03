FROM alpine:latest

RUN apk add --no-cache postfix

RUN postconf -e "inet_interfaces = loopback-only"
RUN postconf -e "mynetworks = 127.0.0.0/8 [::ffff:127.0.0.0]/104 [::1]/128"
RUN postconf -e "mydestination = localhost"
RUN postconf -e "inet_protocols = all"
#RUN postconf -e "myhostname = smtp.axay.net"

CMD ["postfix", "start-fg"]
