The launcher foreground icon was designed in Figma. However, when imported into 
Android Studio, the following errors occurred:

```
ERROR @ line 6: <filter> is not supported
ERROR @ line 7: <feFlood> is not supported
ERROR @ line 8: <feColorMatrix> is not supported
ERROR @ line 9: <feOffset> is not supported
ERROR @ line 10: <feGaussianBlur> is not supported
ERROR @ line 11: <feComposite> is not supported
ERROR @ line 12: <feColorMatrix> is not supported
ERROR @ line 13: <feBlend> is not supported
ERROR @ line 14: <feBlend> is not supported
```

To address this issue, `new.svg` was created using Inkscape. Android Studio 
still shows errors:

```
ERROR @ line 32: <filter> is not supported
ERROR @ line 40: <feGaussianBlur> is not supported 
```

Unlike the previous attempt, the shadow is now visible, though it lacks the 
blur effect.

For more details, see this unanswered question on StackOverflow:  
https://stackoverflow.com/questions/56630125/i-got-message-import-svg-with-error-fegaussianblur-is-not-supported
