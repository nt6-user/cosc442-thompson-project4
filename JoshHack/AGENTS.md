# JoshHack Agent Instructions

- Work from `JoshHack/` using Java 17 and the Maven wrapper: `./mvnw` on Unix-like systems or `mvnw.cmd` on Windows.
- Use JUnit 5 through Maven Surefire. Run the relevant test class during implementation and run `./mvnw test` or `mvnw.cmd test` before completion.
- Run PIT mutation coverage and SpotBugs before work is complete when the task affects source behavior or test quality:
  `./mvnw test-compile org.pitest:pitest-maven:mutationCoverage`
  `./mvnw test-compile com.github.spotbugs:spotbugs-maven-plugin:spotbugs`
- Preserve existing behavior unless a change is explicitly requested.
- Do not weaken, remove, skip, or disable existing tests merely to make the build pass.
- Clearly identify any production-code change and keep it separate from test-only changes for human review.
- Do not push or merge changes automatically; commit or publish only when explicitly requested.
