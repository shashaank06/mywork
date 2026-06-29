# Portfolio Website

This folder contains a Hugo portfolio site built with the Toha theme.

## Local preview

1. Install Hugo:
   - macOS: `brew install hugo`
2. Run:
   - `cd portfolio && hugo server`
3. Open the local URL shown by Hugo.

## Deployment

This repository includes a GitHub Actions workflow to build and deploy the site to the `gh-pages` branch.
After you push the `portfolio-site` branch, GitHub can publish the site from `gh-pages`.

## Customize

- Edit `portfolio/config.yaml` for site settings.
- Edit the author summary and contact info.
- Add project posts under `portfolio/content/posts/`.
