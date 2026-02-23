# GitHub login from Git Bash

Choose one of the following.

---

## Option 1: HTTPS + Personal Access Token (recommended)

GitHub no longer accepts your account password for `git push`. Use a **Personal Access Token (PAT)** instead.

### 1. Create a token on GitHub

1. Go to **GitHub.com** → click your profile picture (top right) → **Settings**.
2. Left sidebar: **Developer settings** → **Personal access tokens** → **Tokens (classic)**.
3. Click **Generate new token** → **Generate new token (classic)**.
4. Set:
   - **Note**: e.g. `samsung-wallet-atw (local)`
   - **Expiration**: 90 days or No expiration (your choice)
   - **Scopes**: check **repo** (full control of private repositories).
5. Click **Generate token**, then **copy the token** (you won’t see it again).

### 2. Use the token in Git Bash

When you run `git push`, Git will ask:

- **Username**: your GitHub username (e.g. `jssechoi`)
- **Password**: paste the **Personal Access Token** (not your GitHub password)

To save credentials so you don’t type them every time:

```bash
git config --global credential.helper store
```

After the next successful login (username + token), Git will remember them.

---

## Option 2: SSH key

Use an SSH key so you can push without typing a password every time.

### 1. Generate SSH key (if you don’t have one)

```bash
ssh-keygen -t ed25519 -C "your_email@example.com"
```

Press Enter to accept the default path (`~/.ssh/id_ed25519`). Optionally set a passphrase.

### 2. Add the public key to GitHub

```bash
cat ~/.ssh/id_ed25519.pub
```

Copy the whole line (starts with `ssh-ed25519`). Then:

1. GitHub → **Settings** → **SSH and GPG keys** → **New SSH key**.
2. Title: e.g. `My PC`.
3. Key: paste the copied line → **Add SSH key**.

### 3. Use SSH URL for the remote

```bash
cd c:/Developments/samsung-wallet-atw-integration
git remote set-url origin git@github.com:jssechoi/samsung-wallet-atw-integration.git
git push -u origin main
```

First time you connect, type `yes` when asked about the host key. No username/password needed after that.

---

## Quick check

After setting up (HTTPS with token or SSH), run:

```bash
cd c:/Developments/samsung-wallet-atw-integration
git push -u origin main
```

- **HTTPS**: prompt for username → enter `jssechoi`, then paste the token as password.
- **SSH**: may ask for your SSH key passphrase once; then push succeeds.
