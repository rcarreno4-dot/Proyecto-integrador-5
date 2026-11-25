import React, { useEffect, useRef } from "react";

export const SnowOverlay: React.FC = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    let animationId: number;

    const resize = () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    };
    resize();
    window.addEventListener("resize", resize);

    const count = Math.min(80, Math.floor(window.innerWidth / 18));
    const snowflakes = Array.from({ length: count }, (_, i) => {
      const isCrystal = i % 4 === 0;
      return {
        x: Math.random() * window.innerWidth,
        y: Math.random() * window.innerHeight,
        radius: isCrystal ? Math.random() * 2.5 + 3.5 : Math.random() * 2.5 + 1.8,
        speedY: Math.random() * 1.3 + 0.65,
        speedX: Math.random() * 0.8 - 0.4,
        opacity: Math.random() * 0.35 + 0.65,
        sway: Math.random() * Math.PI * 2,
        isCrystal,
        rot: Math.random() * Math.PI * 2,
        rotSpeed: (Math.random() - 0.5) * 0.03
      };
    });

    const draw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      for (let i = 0; i < snowflakes.length; i++) {
        const f = snowflakes[i];
        f.sway += 0.015;
        f.x += Math.sin(f.sway) * 0.65 + f.speedX;
        f.y += f.speedY;
        f.rot += f.rotSpeed;

        if (f.y > canvas.height + 15) {
          f.y = -15;
          f.x = Math.random() * canvas.width;
        }
        if (f.x > canvas.width + 15) f.x = -15;
        if (f.x < -15) f.x = canvas.width + 15;

        ctx.save();
        if (f.isCrystal) {
          ctx.translate(f.x, f.y);
          ctx.rotate(f.rot);
          ctx.font = `bold ${Math.round(f.radius * 3.2)}px sans-serif`;
          ctx.textAlign = "center";
          ctx.textBaseline = "middle";
          ctx.strokeStyle = `rgba(2, 132, 199, ${f.opacity * 0.9})`;
          ctx.lineWidth = 1.2;
          ctx.strokeText("❄", 0, 0);
          ctx.fillStyle = `rgba(240, 249, 255, ${f.opacity})`;
          ctx.fillText("❄", 0, 0);
        } else {
          ctx.beginPath();
          ctx.arc(f.x, f.y, f.radius, 0, Math.PI * 2, false);
          ctx.fillStyle = `rgba(248, 250, 252, ${f.opacity})`;
          ctx.strokeStyle = `rgba(2, 132, 199, ${f.opacity * 0.8})`;
          ctx.lineWidth = 1;
          ctx.shadowBlur = 6;
          ctx.shadowColor = `rgba(14, 165, 233, ${f.opacity * 0.85})`;
          ctx.stroke();
          ctx.fill();
        }
        ctx.restore();
      }
      animationId = requestAnimationFrame(draw);
    };

    draw();

    return () => {
      window.removeEventListener("resize", resize);
      cancelAnimationFrame(animationId);
    };
  }, []);

  return (
    <canvas
      ref={canvasRef}
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100vw",
        height: "100vh",
        pointerEvents: "none",
        zIndex: 99999
      }}
      aria-hidden="true"
    />
  );
};
