FROM alpine:latest

ENTRYPOINT ["echo", "[", "$0", "$@", "]"]
CMD ["haha"]