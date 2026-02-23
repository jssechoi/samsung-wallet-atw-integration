# Uploading to GitHub

Follow these steps to publish this project to GitHub.

## 1. Create a repository on GitHub

1. Go to [github.com](https://github.com) and sign in.
2. Click **New repository** (or **+** → **New repository**).
3. Set:
   - **Repository name**: e.g. `samsung-wallet-atw-integration`
   - **Description**: e.g. "Samsung Wallet Add to Wallet (ATW) integration for partners – tickets, boarding pass, coupon, digital ID"
   - **Public** (or Private if you prefer).
   - Do **not** initialize with README, .gitignore, or license (we already have them).
4. Click **Create repository**.

## 2. Initialize Git and push (on your PC)

Open a terminal in the project root (`samsung-wallet-atw-integration`) and run:

```bash
# Initialize Git (if not already)
git init

# Add all files (respects .gitignore)
git add .

# First commit
git commit -m "Initial commit: Samsung Wallet ATW integration demo"

# Add your GitHub repo as remote (replace YOUR_USERNAME and REPO_NAME with yours)
git remote add origin https://github.com/YOUR_USERNAME/samsung-wallet-atw-integration.git

# Push (main branch)
git branch -M main
git push -u origin main
```

If the repo was created with a README and you already had commits:

```bash
git remote add origin https://github.com/YOUR_USERNAME/samsung-wallet-atw-integration.git
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## 3. Optional: SSH instead of HTTPS

If you use SSH keys:

```bash
git remote add origin git@github.com:YOUR_USERNAME/samsung-wallet-atw-integration.git
git push -u origin main
```

## 4. What is not uploaded (.gitignore)

- `target/`, `build/` (build outputs)
- `demo-spring-boot-backend/certs/*.key`, `*.crt`, `*.pem` (real keys/certificates)
- `.idea/`, `.vscode/` (IDE settings)
- `.env`, `application-local.yml` (local secrets)

The `certs/` folder structure and `certs/README.md` are included so others know where to place their keys.

## 5. After first push

- Add **topics** on the repo page (e.g. `samsung-wallet`, `android`, `java`, `spring-boot`).
- In **Settings → General**, set **Default branch** to `main` if needed.
- Optionally add a **LICENSE** file (e.g. MIT, Apache 2.0) in the root and commit.
