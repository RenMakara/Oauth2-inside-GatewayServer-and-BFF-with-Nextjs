
# Using Images in Markdown

To embed images in a README file, use this syntax:

```markdown
![Alt text](image-path)
```

**Examples:**

```markdown
![Logo](./images/logo.png)
![Screenshot](https://example.com/screenshot.jpg)
```

**Parameters:**
- `Alt text`: Description shown if image fails to load
- `image-path`: Relative path or URL to the image

**With size control:**
```markdown
<img src="./images/logo.png" width="200" alt="Logo">
```

**Best practices:**
- Store images in an `./images/` folder
- Use descriptive alt text
- Optimize image file sizes
- Use relative paths for repository images
