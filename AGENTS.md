## Git Workflow

This repository is a Codex learning repository.

Codex may:
- inspect Git history
- create feature branches
- stage relevant files
- create commits
- push feature branches when explicitly requested

Before every commit:

1. Run relevant tests.
2. Run `git status`.
3. Review `git diff`.
4. Stage only files related to the requested change.
5. Verify that no secrets or temporary files are included.
6. Use a conventional commit message.

Commit message examples:

- feat: add product creation endpoint
- fix: handle missing product
- test: add product service tests
- refactor: simplify product mapping
- docs: update API documentation

Never:
- force push
- commit secrets
- use `git reset --hard` without explicit approval
- delete branches without explicit approval
- modify unrelated files

For learning exercises, prefer feature branches instead of committing
directly to main.