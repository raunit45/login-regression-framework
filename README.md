# Login Regression Framework (Selenium Java + TestNG + CSV) - Amazon Target

## Key features
- Regression suite (TestNG) + Page Object Model
- DRY utilities (DriverFactory/WaitUtil/Csv utils)
- **No hardcoded runtime values in Java**: URL, browser, timeouts, file paths, runId prefix come from config/CSV
- **Global WebDriver** via ThreadLocal (industry standard)
- Test cases loaded from CSV (20+)
- Results written to CSV with runId + screenshot path + log path
- Logs written to run-specific file under results/logs/
- Detects CAPTCHA/2FA and marks the case as SKIPPED (no bypass attempts)

## Run
```bash
mvn test
```

## Outputs
- Results CSV: `src/test/resources/results/login_results.csv`
- Screenshots: `src/test/resources/results/screenshots/<runId>/`
- Logs: `src/test/resources/results/logs/<runId>.log`
