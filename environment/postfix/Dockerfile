FROM alpine:latest

RUN apk add postfix
RUN apk update

RUN postconf -e "myhostname = smtp.axay.net"
RUN postconf -e "mydestination = $myhostname, localhost.axay.net, axay.net"
RUN postconf -e "inet_interfaces = loopback_only"
