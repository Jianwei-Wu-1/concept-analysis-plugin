# Concept Analysis Plugin

Concept Analysis Plugin is a IntelliJ IDE plugin for extracting uniqueness of JUnit tests to improve the quality of existing tests.

## Installation

1. Clone current code repository.
2. Import the cloned project into IntelliJ IDEA Ultimate version 2018.2 - 2019.2 (required).
3. Run Gradle build, for any Gradle issues, refer to [Common issues](https://stackoverflow.com/questions/tagged/gradle).

## Usage

1. After Gradle build is successful, click the <b>run</b> button on the top function bar.
2. Another IntelliJ window will pop up and ask to open a project.
3. Use this window to select the project you want to run the analysis on.
4. On top menu, click <b>Code</b>, under <b>Code</b> menu, click <b>RunConceptAnalysis</b>.
5. Plugin will automatically start to perform on the selected project, results will be printed in <b>Run console</b>.

## Contributing

Permission needed.

Please contact Jianwei Wu at <b>wjwcis@udel.edu</b> before any pull request is created.

## Structure

- <b>java</b> folder contains main function to invoke Plugin.
- <b>kotlin</b> folder contains all implementations for concept analysis and top-level/secondary code.
- <b>RunAnalysis</b> starts the Plugin.

## License
MIT License

Copyright (c) [2022] [Jianwei Wu, James Clause]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentations (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OR GUARANTEE-TO-WORK OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.