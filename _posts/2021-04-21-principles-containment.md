---
title: Principles of Containment 
author_staff_member: team
featured_image:  /images/lil-containers.jpeg
---

General claim: The thing doing the containing can see the component it is containing. The latter really only suspects it is contained, and cannot casually reach the container to interact with it explicitly, unless the container configured it to do so. Thus, there is an implicit sandboxing at each container/contained boundary. When we say each container/contained boundary I suggest that these should be nestable and each contained item be further restricted without knowledge of its nesting depth.

![](//paulhammant.com/images/containment.jpg)

# Containment and sandboxing with the Java Virtual Machines

The JVM has the ability to carve up classloaders into hierarchies and within that attach different security managers allowing for:

* implementation hiding
* different permissions files, outgoing site access, incoming socket listening, etc.

Classically, big server technologies like J2EE servers used this to allow some form of multi-tenant capability. Such servers could host multiple apps and protect them from each other in terms of data/method access. Those servers couldn't so easily protect each app from others in terms of CPU or RAM limits and that would also speak to the principles of containment.

Note that Java's JVM tech is a **virtual processor system**. Sources are compiled to this spec and could run on any machine that cam emuate that virtual processor. Compilation would be via the JDK, and the JRE delivers the run anywhere aspect regardless of 32/64bit RISC/CISC differences of the CPU in question. Many other languages don't focus on any virtual processor spec, though increasingly LLVM retrofits other languages with that capability.

## Java based operating systems

As above, an OS that were written in Java could utilize containment and fine-grained permissions within to deliver a more secure application environment.  You could install something on your (say) phone that couldn't reach out of its container and steal things, nor send that outside.  Well, it couldn't unless you'd clicked "yes" to two permissions: "see all data on phone" and "open connections & exchange data to the any web site" when you installed it.

Note: Oracle could make a secure phone OS to rival Android using J2SE, but they choose not to.

---

There are some other examples of containment that don't have fine-grained yet hard permissions as a capability:

# Dependency Injection

Dependency Injection (DI) containers (some say frameworks) came to the fore in the middle 2000's Avalon introduced "Inversion of Control" (IoC). You should be able to instantiate as many wholly separate containers as you need, register components (or other containers) within them. By default, none of those contained things had references to the container that contained it, nor to other containers they were not explicitly told about. Handing the container to the component within it being an anti-pattern. 

Dependency Injection is a more of a thing for Java and C# than it is for Ruby or Python. Of course Java runs in a Java virtual machine, which is a process on an OS, and we historically had container concepts with EAR and WAR file deployments, that were diversion perhaps. Instead of those two we see 'boot' style launchers for apps and services. Some of which still delivery DI container capability (Spring Boot for example).

Note that JavaScript has DI too, first introduced by AngularJS.  Not quite the same as the DOM is wide open to lots of hackery. At least, inside the "same origin" policy. See below.

# Classic Virtual Machines

Think VMWare, Amazon EC2 instances, or the equivalent Digital Ocean (etc) things. Each needs a wholly-separate Operating System, and shares CPU, RAM and networking infrastructure with sibling VMs, but can't casually discover the siblings, or reach into innards of the containing host operating system. The technology on the host that manages the guest VMs may expose/configure file mounts, incoming ports/sockets, and graphical resources, but the directionality is clear.  Each VM can host a number of processes as a normal machine installed OS would have. VMs don't normally configure/restrict outgoing IP addresses/ports, but they should be able to.

# Docker style containers

Not to be confused with VMware style VMs, though they often go together. A host machine (physical or virtual) mounts a container with minimal (shared) operating system resources. It feels like a regular operating system, but conventions apply, and the contained is limited to a single application/service. Typically, that is a limit on the number of larger processes running. Say one ideally, though again that is really only a convention. As before, the container knows about the contained, and the contained is limited in what it can connect to (files/dirs, incoming and outgoing network connections). Deployments focusing on this style of container, always use less resources that deployments resting on VM solutions (above).

Seamless Docker in Docker would be the perfect world for some classes of app. Docker's "compose" functionality is the alternative to that and sets up interdependencies without the container in container aspects we're walking about here.

# User Interfaces

It is less obvious here, but a visual rectangle of a UI that contains many smaller rectangles most likely guides the layout more than the contained guide the layout of the container. Maybe the container queries the contained about preferred size and all that. This is all about the 'V' in MVC (Model View Controller), and the M and C aspects are entirely different programmatic constructs. As you'd expect, all bets are off if there is absolute positioning going on.

There are strong component models like Java's Swing framework (which Netscape donated to Sun in 1997 or thereabouts). The principles of containment are almost never fully realised though, as the view assembly code can navigate casually to its own container and tinker with it, or sibling components/containers.

Even for Flash applications in web-apps (seemingly a rectangle in a rectangle), the principle of containment was weak as a technology called LiveConnect allowed the Flash "control" to reach back out into DOM of the container, although maybe the same origin applied. Flash is dead anyway, and nothing programmatic has assumed its place in the world of browser plugins.

## Web applications specifically

What a disaster. As with fat UIs the components in a browser tab can interfere with each other, regardless of the visual containment intentions.  This is so bad today that you can't have rectangles from modern web microframeworks (say AngularJS, Angular(2), EmberJS, and React) share space on the same page and have easy isolation. It just breaks as the monkey-patching of bits of pieces of the DOM to get things working destabilises things. Say AngularJS inadvertently disables EmberJS, or vice versa, or each effectively kills the other. You have to revert to Iframes to guarantee isolation, and that is quite heavy with big aesthetic downsides.

I say disaster because it is 2016 and we are 25+ years into the web and this should have been solved many years before. &lt;div isolate&gt;..&lt;/div&gt; is what we needed, as well as a mechanism to declare inputs and outputs of the contained, and wide support for a HTML-import. Note: &lt;iframe&gt; specifically has had a 'sandbox' feature since HTML v5 and in mainstream compatibility since 2014.

Circling back to Dependency Injection - this pattern/practice simplified the construction of non-visual parts of the UI. It is a trick though, as the IoC can be subverted within the DOM. Components/Services/Directives can gain a reference to their implicit containers after a few lines of JavaScript.

Maybe in another decade the DOM will become a better container/contained facilitator.

(pic by Clare Marino via [Unsplash](https://unsplash.com/photos/EGXpSrG02sU))