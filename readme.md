# Spamgourmet Reloaded

This project is a complete remake of the original [spamgourmet](https://spamgourmet.com) service - built with Kotlin. Its aim is to modernize the user experience and extend the project with additional features.

## Functionality

Once signed up, you have an unchangeable *address name*.

Now you can create an infinite amount of addresses which can be used to
   1. obfuscate your real email address
   2. limit the amount of emails you receive through that address

Let's say your *spamgourmet address name* is "foo", you are creating an account for "barweb" and (at the moment) want to receive a maximum of "12" emails through that address, then you could use the following email address:
   - `barweb.12.foo@axay.net`

Additionally, to this core functionality, spamgourmet reloaded offers you to:
   - handle bounces (in all directions)
   - add trusted senders
   - set up forwarding rules
   - answer (which also obfuscates your real address)
   - and more (all through the dashboard)

## Project

### Mailserver

The mailserver is the "core" of the service. It receives, processes and forwards emails. It is written in Kotlin (JVM) and uses [SubEtha SMTP](https://github.com/voodoodyne/subethasmtp) under the hood.

### Webserver

The webserver provides the user interface. It allows the user to create and manage his account. It is written in Kotlin (JVM) and uses [Ktor](https://ktor.io/).

Spamgourmet does not use any HTML templating language, instead all web pages are built using [kotlinx.html](https://github.com/Kotlin/kotlinx.html), which is faster and more flexible (but it does not have great IDE support like HTML does).

On the clientside, the webserver serves javascript which was compiled from [Kotlin/JS](https://kotlinlang.org/docs/reference/js-overview.html).

### Work in progress

Currently, the service is still in development. It is planned to:
   - use Docker
   - use Tailwind CSS for styling

### Naming

Obfuscated addresses are called a *user address*. All addresses that send an email to a user address, regardless of the emails content, are called a *spammer address*.