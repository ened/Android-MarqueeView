Android-MarqueeView
===================

MarqueeView which supports custom timings.

It can be used to fine-tune the animation speed and the pause between each movement.

Usage
=====

Simply add a single TextView within the MarqueeView instance.

The view can be configured via Java code, or through XML. Please see the sample for details.

Including In Your Project
-------------------------

If you are a Maven user you can easily include the library by specifying it as
a dependency:

    <dependency>
      <groupId>asia.ivity.android</groupId>
      <artifactId>marqueeview-apklib</artifactId>
      <version>{insert-latest-version}</version>
      <type>apklib</type>
    </dependency>

If you are using gradle, you can refer to the .aar version like so:

    // Ensure to fetch dependencies from maven central
    repositories {
      mavenCentral()
    }

    dependencies {
      compile 'asia.ivity.android:marqueeview:{insert-latest-version}@aar'
    }
  
Developed By
============

* Sebastian Roth - <sebastian.roth@gmail.com>

License
=======

  Copyright (c) 2013, Sebastian Roth
  
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.